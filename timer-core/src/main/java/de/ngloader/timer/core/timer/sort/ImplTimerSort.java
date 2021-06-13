package de.ngloader.timer.core.timer.sort;

import de.ngloader.timer.api.config.Exclude;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.sort.TimerSort;

public abstract class ImplTimerSort implements TimerSort {

	@Exclude
	private final Timer timer;

	public ImplTimerSort(Timer timer) {
		this.timer = timer;
	}

	@Override
	public void run() { }

	@Override
	public void enable() { }

	@Override
	public void disable() { }

	@Override
	public Timer getTimer() {
		return this.timer;
	}
}