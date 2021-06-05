package de.ngloader.timer.api.timer.sort.type;

import de.ngloader.timer.api.timer.sort.TimerSort;

public interface TimerSortRoundRobin extends TimerSort {

	public int getIndex();
	public void setIndex(int index);
}