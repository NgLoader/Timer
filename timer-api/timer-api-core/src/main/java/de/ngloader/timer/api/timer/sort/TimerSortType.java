package de.ngloader.timer.api.timer.sort;

import de.ngloader.timer.api.timer.sort.type.TimerSortRandom;
import de.ngloader.timer.api.timer.sort.type.TimerSortRoundRobin;

public enum TimerSortType {

	RANDOM(TimerSortRandom.class),
	ROUNDROBIN(TimerSortRoundRobin.class);

	private final Class<? extends TimerSort> classType;

	private TimerSortType(Class<? extends TimerSort> clazzType) {
		this.classType = clazzType;
	}

	public Class<? extends TimerSort> getClassType() {
		return classType;
	}
}
