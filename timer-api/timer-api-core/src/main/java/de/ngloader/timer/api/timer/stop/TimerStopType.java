package de.ngloader.timer.api.timer.stop;

import de.ngloader.timer.api.timer.stop.type.TimerStopDelete;
import de.ngloader.timer.api.timer.stop.type.TimerStopNone;
import de.ngloader.timer.api.timer.stop.type.TimerStopReset;
import de.ngloader.timer.api.timer.stop.type.TimerStopStop;

public enum TimerStopType {

	DELETE(TimerStopDelete.class),
	NONE(TimerStopNone.class),
	RESET(TimerStopReset.class),
	STOP(TimerStopStop.class);

	private final Class<? extends TimerStop> classType;

	private TimerStopType(Class<? extends TimerStop> clazzType) {
		this.classType = clazzType;
	}

	public Class<? extends TimerStop> getClassType() {
		return classType;
	}
}
