package de.ngloader.timer.api.timer.sort.type;

import java.util.Random;

import de.ngloader.timer.api.timer.sort.TimerSort;

public interface TimerSortRandom extends TimerSort {

	public Random getRandom();
	public void setRandom(Random random);
}