package de.ngloader.timer.core.timer.sort.type;

import java.util.List;
import java.util.Random;

import de.ngloader.timer.api.config.Exclude;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.api.timer.sort.type.TimerSortRandom;
import de.ngloader.timer.core.timer.sort.ImplTimerSort;

public class ImplTimerSortRandom extends ImplTimerSort implements TimerSortRandom {

	@Exclude
	private Random random = new Random();

	public ImplTimerSortRandom(Timer timer) {
		super(timer);
	}

	@Override
	public TimerMessage getNextMessage() {
		List<TimerMessage> messages = this.getTimer().getMessages();
		return messages.get(this.random.nextInt(messages.size()));
	}

	@Override
	public Random getRandom() {
		return this.random;
	}

	@Override
	public void setRandom(Random random) {
		this.random = random;
	}
}