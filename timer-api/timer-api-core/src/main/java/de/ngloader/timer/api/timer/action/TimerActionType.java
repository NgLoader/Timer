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

	public static TimerActionType search(String name) {
		name = name.toLowerCase();

		for (TimerActionType type : values()) {
			if (type.className.startsWith(name)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerAction> classType;
	private final String className;

	private TimerActionType(Class<? extends TimerAction> clazzType) {
		this.classType = clazzType;

		this.className = this.classType.getSimpleName().toLowerCase();
	}

	public Class<? extends TimerAction> getClassType() {
		return classType;
	}
}
