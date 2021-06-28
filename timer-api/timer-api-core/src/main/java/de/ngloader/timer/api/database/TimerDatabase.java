package de.ngloader.timer.api.database;

import java.util.Set;

import de.ngloader.timer.api.timer.Timer;

public interface TimerDatabase {

	public void openConnection();
	public void closeConnection();
	public boolean isConnected();

	public Set<Timer> getTimer();

	public void createTimer(Timer timer);
	public void updateTimer(Timer timer);
	public void deleteTimer(Timer timer);

	public TimerDatabaseType getType();
}
