package de.ngloader.timer.api;

import de.ngloader.timer.api.config.Config;
import de.ngloader.timer.api.database.TimerDatabaseType;

@Config(name = "config.json")
public class TimerConfig {
	public TimerDatabaseType databaseType = TimerDatabaseType.JSON;
}
