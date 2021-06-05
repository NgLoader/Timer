package de.ngloader.timer.core.timer.action.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.action.type.TimerActionCountdown;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.core.timer.action.ImplTimerAction;

public class ImplTimerActionCountdown extends ImplTimerAction implements TimerActionCountdown {

	public ImplTimerActionCountdown(Timer timer) {
		super(timer);
	}

	@Override
	public void onTick(long tick) {
		this.getTimer().getMessages().stream().filter(message -> message.getTick() == tick).forEach(TimerMessage::run);
	}
}