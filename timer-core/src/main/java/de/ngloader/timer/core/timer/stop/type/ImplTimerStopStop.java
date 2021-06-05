package de.ngloader.timer.core.timer.stop.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.stop.type.TimerStopStop;
import de.ngloader.timer.core.timer.stop.ImplTimerStop;

public class ImplTimerStopStop extends ImplTimerStop implements TimerStopStop {

	public ImplTimerStopStop(Timer timer) {
		super(timer);
	}

	@Override
	public void run() {
		this.getTimer().setEnabled(false);
	}
}