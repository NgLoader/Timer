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

	public static TimerStopType getType(Class<? extends TimerStop> clazz) {
		for (TimerStopType type : values()) {
			if (type.getClassType().isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
	}

	public static TimerStopType search(String name) {
		name = name.toLowerCase();

		for (TimerStopType type : values()) {
			if (type.className.startsWith(name)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerStop> classType;
	private final String className;

	private TimerStopType(Class<? extends TimerStop> clazzType) {
		this.classType = clazzType;

		this.className = this.classType.getSimpleName().toLowerCase();
	}

	public Class<? extends TimerStop> getClassType() {
		return classType;
	}
}
