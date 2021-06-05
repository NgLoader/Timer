package de.ngloader.timer.core.timer.sort.type;

import java.util.List;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.api.timer.sort.type.TimerSortRoundRobin;
import de.ngloader.timer.core.timer.sort.ImplTimerSort;

public class ImplTimerSortRoundRobin extends ImplTimerSort implements TimerSortRoundRobin {

	private int index = 0;

	public ImplTimerSortRoundRobin(Timer timer) {
		super(timer);
	}

	@Override
	public TimerMessage getNextMessage() {
		List<TimerMessage> messages = this.getTimer().getMessages();
		if (this.index >= messages.size()) {
			this.index = 0;
		}

		return messages.get(this.index++);
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}
}