package de.ngloader.timer.api.timer.action;

import de.ngloader.timer.api.timer.action.type.TimerActionCountdown;
import de.ngloader.timer.api.timer.action.type.TimerActionMessage;

public enum TimerActionType {

	COUNTDOWN(TimerActionCountdown.class),
	MESSAGE(TimerActionMessage.class);

	public static TimerActionType getType(Class<? extends TimerAction> clazz) {
		for (TimerActionType type : values()) {
			if (type.getClassType().isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerAction> classType;

	private TimerActionType(Class<? extends TimerAction> clazzType) {
		this.classType = clazzType;
	}

	public Class<? extends TimerAction> getClassType() {
		return classType;
	}
}
