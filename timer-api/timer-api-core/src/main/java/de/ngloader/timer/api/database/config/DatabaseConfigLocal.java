package de.ngloader.timer.api.database.config;

import de.ngloader.timer.api.config.Config;

@Config(path = "database", name = "local")
public class DatabaseConfigLocal {
	public String storagePath = "./localstorage";
}