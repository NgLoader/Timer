package de.ngloader.timer.api.timer.sort;

import de.ngloader.timer.api.timer.sort.type.TimerSortRandom;
import de.ngloader.timer.api.timer.sort.type.TimerSortRoundRobin;

public enum TimerSortType {

	RANDOM(TimerSortRandom.class),
	ROUNDROBIN(TimerSortRoundRobin.class);

	public static TimerSortType getType(Class<? extends TimerSort> clazz) {
		for (TimerSortType type : values()) {
			if (type.getClassType().isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
	}

	public static TimerSortType search(String name) {
		name = name.toLowerCase();

		for (TimerSortType type : values()) {
			if (type.className.startsWith(name)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerSort> classType;
	private final String className;

	private TimerSortType(Class<? extends TimerSort> clazzType) {
		this.classType = clazzType;

		this.className = this.classType.getSimpleName().toLowerCase();
	}

	public Class<? extends TimerSort> getClassType() {
		return classType;
	}
}
