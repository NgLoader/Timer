package de.ngloader.timer.core.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.database.TimerDatabaseType;
import de.ngloader.timer.api.database.config.DatabaseConfigJson;
import de.ngloader.timer.api.timer.Timer;

public class ImplTimerDatabaseJson implements TimerDatabase {

	private final TimerPlugin plugin;
	private final DatabaseConfigJson config;
	private boolean open = false;

	public ImplTimerDatabaseJson(TimerPlugin plugin) {
		this.plugin = plugin;
		this.config = this.plugin.getConfigService().getConfig(DatabaseConfigJson.class);
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

		Set<Timer> timers = new HashSet<>();
		try (Stream<Path> paths = Files.walk(Paths.get(config.storagePath))) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timers;
	}

	@Override
	public void createTimer(Timer timer) {
		if (!this.open) {
			return;
		}
	}

	@Override
	public void updateTimer(Timer timer) {
		if (!this.open) {
			return;
		}
	}

	@Override
	public void deleteTimer(Timer timer) {
		if (!this.open) {
			return;
		}
	}

	@Override
	public TimerDatabaseType getType() {
		return TimerDatabaseType.JSON;
	}
}