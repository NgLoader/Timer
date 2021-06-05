package de.ngloader.timer.core.timer.message.type;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.message.type.TimerMessageChat;
import de.ngloader.timer.core.timer.message.ImplTimerMessage;

public class ImplTimerMessageChat extends ImplTimerMessage implements TimerMessageChat {

	public ImplTimerMessageChat(Timer timer) {
		super(timer);
	}

	public ImplTimerMessageChat(Timer timer, long tick, String content, String permission) {
		super(timer, tick, content, permission);
	}

	@Override
	public void run() {
		Timer timer = this.getTimer();
		timer.getPlugin().sendChatMessage(timer.getSortType().getNextMessage().getContent(), this.getPermission());
	}
}