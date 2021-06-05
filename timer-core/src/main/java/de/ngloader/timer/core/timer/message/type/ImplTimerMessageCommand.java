package de.ngloader.timer.core.timer.message.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.message.type.TimerMessageChat;
import de.ngloader.timer.core.timer.message.ImplTimerMessage;

public class ImplTimerMessageCommand extends ImplTimerMessage implements TimerMessageChat {

	public ImplTimerMessageCommand(Timer timer) {
		super(timer);
	}

	public ImplTimerMessageCommand(Timer timer, long tick, String content, String permission) {
		super(timer, tick, content, permission);
	}

	@Override
	public void run() {
		Timer timer = this.getTimer();
		timer.getPlugin().sendChatCommand(timer.getSortType().getNextMessage().getUncoloredContent(), this.getPermission());
	}
}