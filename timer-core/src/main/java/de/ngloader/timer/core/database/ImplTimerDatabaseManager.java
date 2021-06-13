package de.ngloader.timer.core.database;

import java.util.Objects;
import java.util.Set;

import de.ngloader.timer.api.TimerConfig;
import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.database.TimerDatabaseManager;
import de.ngloader.timer.api.database.TimerDatabaseType;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;

public class ImplTimerDatabaseManager implements TimerDatabaseManager {

	private final TimerPlugin plugin;
	private TimerDatabase database;

	public ImplTimerDatabaseManager(TimerPlugin plugin) {
		this.plugin = plugin;

		this.setDatabase(plugin.getConfigService().getConfig(TimerConfig.class).databaseType);
	}

	private TimerDatabase createDatabase(TimerDatabaseType type) {
		if (type == this.database.getType()) {
			return this.database;
		}

		switch (type) {
		case JSON:
			return new ImplTimerDatabaseJson(this.plugin);

		case MONGODB:
			return null;

		case MYSQL:
			return null;

		default:
			throw new NullPointerException("Unable to find database adapter for \"" + type.name() + "\"!");
		}
	}

	@Override
	public TimerDatabase getDatabase() {
		return this.database;
	}

	@Override
	public void setDatabase(TimerDatabaseType type) {
		Objects.requireNonNull(type, "Database type is null");

		this.setDatabase(this.createDatabase(type));
	}

	@Override
	public void setDatabase(TimerDatabase database) {
		Objects.requireNonNull(database, "Database is null");

		TimerManager manager = this.plugin.getDefaultManager();
		try {
			if (this.database == null) {
				return;
			}

			this.plugin.log("Database", "Storing timers to database§8.");
			manager.getTimers().forEach(this.database::updateTimer);
			this.plugin.log("Database", "Closing database connection§8.");
			this.database.closeConnection();
			this.plugin.log("Database", "Closed database connection§8.");
			manager.getTimers().forEach(manager::removeTimer);
			this.plugin.log("Database", "Removed all timers§8.");
		} finally {
			this.database = database;
			this.plugin.log("Database", "Opening database connection§8.");
			this.database.openConnection();
			this.plugin.log("Database", "Opened database connection§8.");
			this.plugin.log("Database", "Loading timers§8.");
			this.database.getTimer().forEach(manager::addTimer);
			this.plugin.log("Database", "Loaded §8" + manager.getTimers().size() + " §7timer§8.");
		}
	}

	@Override
	public void convert(TimerDatabaseType from, TimerDatabaseType to) {
		Objects.requireNonNull(from, "Database from is null");
		Objects.requireNonNull(to, "Database to is null");

		this.convert(this.createDatabase(from), this.createDatabase(to));
	}

	@Override
	public void convert(TimerDatabase from, TimerDatabase to) {
		Objects.requireNonNull(from, "Database from is null");
		Objects.requireNonNull(to, "Database to is null");

		this.plugin.log("Database", "Converting timers from §8\"§e" + from.getType().name() + "§8\" §8to §8\"§e" + to.getType().name() + "§8\"§8.");
		from.openConnection();
		Set<Timer> timers = from.getTimer();
		this.plugin.log("Database", "Found §e" + timers.size() + " §7timer§8.");
		from.closeConnection();

		to.openConnection();
		timers.forEach(to::createTimer);
		if (to.getType() != this.database.getType()) {
			to.closeConnection();
		}
		this.plugin.log("Database", "Copied §e" + timers.size() + " §7timer§8.");
		this.plugin.log("Database", "Converting finished.");
	}
}