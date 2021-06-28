package de.ngloader.timer.core.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.config.ConfigService;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.database.TimerDatabaseType;
import de.ngloader.timer.api.database.config.DatabaseConfigJson;
import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.core.timer.ImplTimer;

public class ImplTimerDatabaseJson implements TimerDatabase {

	private final TimerPlugin plugin;
	private final ConfigService configService;
	private final DatabaseConfigJson config;
	private boolean open = false;

	public ImplTimerDatabaseJson(TimerPlugin plugin) {
		this.plugin = plugin;
		this.configService = this.plugin.getConfigService();
		this.config = this.configService.getConfig(DatabaseConfigJson.class);
	}

	@Override
	public void openConnection() {
		this.open = true;
	}

	@Override
	public void closeConnection() {
		this.open = false;
	}

	@Override
	public Set<Timer> getTimer() {
		if (!this.open) {
			return Collections.emptySet();
		}

		Path file = this.checkDirection();
		if (file == null) {
			return Collections.emptySet();
		}

		Set<Timer> timers = new HashSet<>();
		try {
			Files.walkFileTree(file, new SimpleFileVisitor<Path>() {

				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".json")) {
						try (BufferedReader bufferedReader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
							ImplTimer timer = ImplTimerDatabaseJson.this.configService.getGson().fromJson(bufferedReader, ImplTimer.class);
							TimerPlugin plugin = ImplTimerDatabaseJson.this.plugin;

							timers.add(new ImplTimer(plugin, plugin.getDefaultManager(), timer));
						} catch (IOException e) {
							ImplTimerDatabaseJson.this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
						}
					}
					return FileVisitResult.CONTINUE;
				};
			});
		} catch (IOException e) {
			this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
		}
		return timers;
	}

	@Override
	public void createTimer(Timer timer) {
		if (!this.open) {
			return;
		}

		Path file = this.checkDirection();
		if (file == null) {
			return;
		}

		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(
				Paths.get(String.format("%s/%s.json", file.toString(), timer.getId().toString())),
				StandardCharsets.UTF_8,
				StandardOpenOption.CREATE)) {
			this.configService.getGson().toJson(timer, bufferedWriter);
		} catch (IOException e) {
			this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
		}
	}

	@Override
	public void updateTimer(Timer timer) {
		if (!this.open) {
			return;
		}

		Path file = this.checkDirection();
		if (file == null) {
			return;
		}

		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(
				Paths.get(String.format("%s/%s.json", file.toString(), timer.getId().toString())),
				StandardCharsets.UTF_8,
				StandardOpenOption.CREATE)) {
			this.configService.getGson().toJson(timer, bufferedWriter);
		} catch (IOException e) {
			this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
		}
	}

	@Override
	public void deleteTimer(Timer timer) {
		if (!this.open) {
			return;
		}

		Path file = this.checkDirection();
		if (file == null) {
			return;
		}

		try {
			Files.deleteIfExists(Paths.get(String.format("%s/%s.json", file.toString(), timer.getId().toString())));
		} catch (IOException e) {
			this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
		}
	}

	private Path checkDirection() {
		try {
			Path file = Paths.get(String.format("./plugins/Timer/%s", this.config.storagePath));
			if (Files.notExists(file)) {
				Files.createDirectories(file);
			}
			return file;
		} catch (IOException e) {
			this.plugin.logError(TimerModule.MODULE_DATABASE, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public TimerDatabaseType getType() {
		return TimerDatabaseType.JSON;
	}

	@Override
	public boolean isConnected() {
		return this.open;
	}
}