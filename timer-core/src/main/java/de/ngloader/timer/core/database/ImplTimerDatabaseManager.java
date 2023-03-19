package de.ngloader.timer.core.database;

import java.util.Objects;
import java.util.Set;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.database.TimerDatabaseManager;
import de.ngloader.timer.api.database.TimerDatabaseType;
import de.ngloader.timer.api.database.config.DatabaseConfigLocal;
import de.ngloader.timer.api.database.config.DatabaseConfigMongoDB;
import de.ngloader.timer.api.database.config.DatabaseConfigMySQL;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;

public class ImplTimerDatabaseManager implements TimerDatabaseManager {

	private final TimerPlugin plugin;
	private TimerDatabase database;

	public ImplTimerDatabaseManager(TimerPlugin plugin) {
		this.plugin = plugin;
	}

	private TimerDatabase createDatabase(TimerDatabaseType type) {
		if (this.database != null && type == this.database.getType()) {
			return this.database;
		}

		this.plugin.getConfigService().loadConfig(DatabaseConfigLocal.class);
		this.plugin.getConfigService().loadConfig(DatabaseConfigMongoDB.class);
		this.plugin.getConfigService().loadConfig(DatabaseConfigMySQL.class);

		switch (type) {
		case LOCAL:
			return new ImplTimerDatabaseLocal(this.plugin);

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

			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_STORING_TIMERS_TO_DATABASE);
			manager.getTimers().forEach(this.database::updateTimer);
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_CLOSING_DATABASE_CONNECTION);
			this.database.closeConnection();
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_CLOSED_DATABASE_CONNECTION);
			manager.getTimers().forEach(manager::removeTimer);
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_REMOVED_ALL_TIMERS);
		} finally {
			this.database = database;
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_OPENING_DATABASE_CONNECTION);
			this.database.openConnection();
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_OPENED_DATABASE_CONNECTION);
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_LOADING_TIMER);
			this.database.getTimer().forEach(manager::addTimer);
			this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_LOADED_TIMER, manager.getTimers().size());
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

		this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_CONVERTING_TIMERS_FROM_TO, from.getType().name(), to.getType().name());
		from.openConnection();
		Set<Timer> timers = from.getTimer();
		this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_FOUND_TIMER, timers.size());
		from.closeConnection();

		to.openConnection();
		timers.forEach(to::createTimer);
		if (to.getType() != this.database.getType()) {
			to.closeConnection();
		}
		this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_COPIED_TIMER, timers.size());
		this.plugin.log(TimerModule.MODULE_DATABASE, TimerMessage.DATABASE_CONVERTING_FINISHED);
	}
}