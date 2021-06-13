package de.ngloader.timer.core.timer.stop;

import de.ngloader.timer.api.config.Exclude;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.stop.TimerStop;

public class ImplTimerStop implements TimerStop {

	@Exclude
	private final Timer timer;

	public ImplTimerStop(Timer timer) {
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