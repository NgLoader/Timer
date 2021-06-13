package de.ngloader.timer.api.database;

public interface TimerDatabaseManager {

	public TimerDatabase getDatabase();
	public void setDatabase(TimerDatabaseType type);
	public void setDatabase(TimerDatabase database);

	public void convert(TimerDatabaseType from, TimerDatabaseType to);
	public void convert(TimerDatabase from, TimerDatabase to);
}
