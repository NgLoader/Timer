package de.ngloader.timer.core.timer.stop.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.stop.type.TimerStopDelete;
import de.ngloader.timer.core.timer.stop.ImplTimerStop;

public class ImplTimerStopDelete extends ImplTimerStop implements TimerStopDelete {

	public ImplTimerStopDelete(Timer timer) {
		super(timer);
	}

	@Override
	public void run() {
		this.getTimer().getPlugin().deleteTimer(this.getTimer());
	}
}