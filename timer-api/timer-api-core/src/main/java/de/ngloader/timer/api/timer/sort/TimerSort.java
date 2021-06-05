package de.ngloader.timer.api.timer.sort;

import de.ngloader.timer.api.timer.TimerType;
import de.ngloader.timer.api.timer.message.TimerMessage;

public interface TimerSort extends TimerType<TimerSort> {

	public TimerMessage getNextMessage();
}