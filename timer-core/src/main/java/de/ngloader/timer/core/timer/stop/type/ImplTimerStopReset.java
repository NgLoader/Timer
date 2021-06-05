package de.ngloader.timer.core.timer.stop.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.stop.type.TimerStopReset;
import de.ngloader.timer.core.timer.stop.ImplTimerStop;

public class ImplTimerStopReset extends ImplTimerStop implements TimerStopReset {

	public ImplTimerStopReset(Timer timer) {
		super(timer);
	}

	@Override
	public void run() {
		this.getTimer().setCurrentTick(0);
	}
}