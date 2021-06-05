package de.ngloader.timer.core.timer.stop.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.stop.type.TimerStopNone;
import de.ngloader.timer.core.timer.stop.ImplTimerStop;

public class ImplTimerStopNone extends ImplTimerStop implements TimerStopNone {

	public ImplTimerStopNone(Timer timer) {
		super(timer);
	}
}