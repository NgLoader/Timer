package de.ngloader.timer.api.config;

import java.nio.file.Path;

import com.google.gson.Gson;

public interface ConfigService {

	public void addTypeAdapter(TypeAdapter<?>... adapters);

	public <T> void loadConfig(Class<? extends T> configClass);

	public void saveConfig(Class<?> configClass);

	public void saveConfig(Object config);

	public <T> T getConfig(Class<T> configClass);

	public <T> T reloadConfig(Class<T> configClass);

	public <T> T removeConfig(T config);

	public <T> T removeConfig(Class<T> configClass);

	public void clearConfigCache();

	public Config getConfigSettings(Class<?> configClass);

	public Path getPath(Config config);

	public Gson getGson();
}