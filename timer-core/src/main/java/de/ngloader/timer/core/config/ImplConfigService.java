package de.ngloader.timer.core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.config.Config;
import de.ngloader.timer.api.config.ConfigService;
import de.ngloader.timer.api.config.TypeAdapter;

public class ImplConfigService implements ConfigService {

	private final TimerPlugin plugin;

	private final Map<Class<?>, Object> configs = new ConcurrentHashMap<>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	private final GsonBuilder gsonBuilder = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping();
	private Gson gson = this.gsonBuilder.create();

	public ImplConfigService(TimerPlugin plugin) {
		this.plugin = plugin;

		ExcludeStrategy excludeStrategy = new ExcludeStrategy();
		this.gsonBuilder.addDeserializationExclusionStrategy(excludeStrategy);
		this.gsonBuilder.addSerializationExclusionStrategy(excludeStrategy);
		this.addTypeAdapter(new TypeAdapterUUID());
	}

	@Override
	public void addTypeAdapter(TypeAdapter<?>... adapters) {
		for (TypeAdapter<?> adapter : adapters) {
			this.gsonBuilder.registerTypeAdapter(adapter.getType(), adapter);
		}
		this.gson = this.gsonBuilder.create();
	}

	@Override
	public <T> void loadConfig(Class<? extends T> configClass) {
		Objects.requireNonNull(configClass, "Config class was null");

		Config annotation = configClass.getDeclaredAnnotation(Config.class);
		if (annotation == null) {
			this.plugin.logError("ConfigService", String.format("'%s' has not the annotation '%s'", configClass.getSimpleName(), Config.class.getSimpleName()));
			return;
		}

		Path path = getPath(annotation);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path.getParent());

				try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
					T instance = configClass.getDeclaredConstructor().newInstance();
					this.gson.toJson(instance, bufferedWriter);
				} catch (Exception e) {
					this.plugin.logError("ConfigService", String.format("Failed to write file '%s'", annotation.path()), e);
					return;
				}
			} catch (IOException e) {
				this.plugin.logError("ConfigService", String.format("Failed to create config '%s'", configClass.getSimpleName()), e);
				return;
			}
		}

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			T config = this.gson.fromJson(reader, configClass);
			if (config == null) {
				this.plugin.logError("ConfigService", String.format("Error by reading json file '%s'", annotation.path()));
				return;
			}

			this.lock.writeLock().lock();
			this.configs.put(configClass, config);
		} catch (Exception e) {
			this.plugin.logError("ConfigService", String.format("Error loading file '%s'", annotation.path()), e);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	@Override
	public void saveConfig(Class<?> configClass) {
		Objects.requireNonNull(configClass, "ConfigClass is null");

		Object config = this.getConfig(configClass);
		this.saveConfig(config);
	}

	@Override
	public void saveConfig(Object config) {
		Objects.requireNonNull(config, "Config is null");

		Config annotation = getConfigSettings(config.getClass());
		Path path = Paths.get(annotation.path());

		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path.getParent());
			} catch (IOException e) {
				this.plugin.logError("ConfigService", String.format("Failed to config directory '%s'", annotation.path()), e);
				return;
			}
		}

		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
			this.gson.toJson(config, bufferedWriter);
		} catch (IOException e) {
			this.plugin.logError("ConfigService", String.format("Failed to write file '%s'", annotation.path()), e);
		}
	}

	@Override
	public <T> T getConfig(Class<T> configClass) {
		Objects.requireNonNull(configClass, "ConfigClass is null");

		this.lock.readLock().lock();
		if (!this.configs.containsKey(configClass)) {
			try {
				this.lock.readLock().unlock();
				this.loadConfig(configClass);
			} finally {
				this.lock.readLock().lock();
			}
		}

		T config = configClass.cast(this.configs.get(configClass));
		this.lock.readLock().unlock();
		return config;
	}

	@Override
	public <T> T reloadConfig(Class<T> configClass) {
		Objects.requireNonNull(configClass, "ConfigClass is null");

		this.removeConfig(configClass);
		return this.getConfig(configClass);
	}

	@Override
	public <T> T removeConfig(T config) {
		Objects.requireNonNull(config, "Config is null");

		Object result = this.removeConfig(config.getClass());
		return result != null ? config : null;
	}

	@Override
	public <T> T removeConfig(Class<T> configClass) {
		Objects.requireNonNull(configClass, "ConfigClass is null");

		try {
			this.lock.writeLock().lock();
			Object config = this.configs.remove(configClass);
			return config != null ? configClass.cast(config) : null;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	@Override
	public void clearConfigCache() {
		try {
			this.lock.writeLock().lock();
			this.configs.clear();
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	@Override
	public Config getConfigSettings(Class<?> configClass) {
		Objects.requireNonNull(configClass, "ConfigClass is null");

		Config config = configClass.getDeclaredAnnotation(Config.class);
		if (config == null) {
			this.plugin.logError("ConfigService", String.format("'%s' has not the annotation '%s'", configClass.getSimpleName(), Config.class.getSimpleName()));
			return null;
		}
		return config;
	}

	@Override
	public Path getPath(Config config) {
		Objects.requireNonNull(config, "Config is null");

		return Paths.get(String.format("./plugins/%s/%s.json", config.path(), config.name()));
	}

	@Override
	public Gson getGson() {
		return this.gson;
	}
}