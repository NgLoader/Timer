package de.ngloader.timer.api.timer.message;

import de.ngloader.timer.api.timer.message.type.TimerMessageChat;
import de.ngloader.timer.api.timer.message.type.TimerMessageCommand;
import de.ngloader.timer.api.timer.message.type.TimerMessageExecute;

public enum TimerMessageType {

	CHAT(TimerMessageChat.class),
	COMMAND(TimerMessageCommand.class),
	EXECUTE(TimerMessageExecute.class);

	public static TimerMessageType getType(Class<? extends TimerMessage> clazz) {
		for (TimerMessageType type : values()) {
			if (type.getClassType().isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerMessage> classType;

	private TimerMessageType(Class<? extends TimerMessage> clazzType) {
		this.classType = clazzType;
	}

	public Class<? extends TimerMessage> getClassType() {
		return classType;
	}
}
