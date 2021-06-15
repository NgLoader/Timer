package de.ngloader.timer.api.database.config;

import de.ngloader.timer.api.config.Config;

@Config(path = "database", name = "json")
public class DatabaseConfigJson {
	public String storagePath = "./localstorage";
}