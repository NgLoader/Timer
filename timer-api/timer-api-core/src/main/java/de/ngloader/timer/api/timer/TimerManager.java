package de.ngloader.timer.api.timer;

import java.util.Set;

public interface TimerManager extends Runnable {

	public boolean addTimer(Timer timer);
	public boolean removeTimer(Timer timer);
	public Timer getTimer(String name);
	public Set<Timer> getTimers();
}