package de.ngloader.timer.core.timer.action.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.action.type.TimerActionCountdown;
import de.ngloader.timer.core.timer.action.ImplTimerAction;

public class ImplTimerActionMessage extends ImplTimerAction implements TimerActionCountdown {

	public ImplTimerActionMessage(Timer timer) {
		super(timer);
	}

	@Override
	public void onTick(long tick) { }

	@Override
	public void run() {
		this.getTimer().getSortType().getNextMessage().run();
	}
}