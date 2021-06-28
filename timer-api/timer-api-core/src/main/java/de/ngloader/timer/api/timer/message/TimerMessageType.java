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

	public static TimerMessageType search(String name) {
		name = name.toLowerCase();

		for (TimerMessageType type : values()) {
			if (type.className.startsWith(name)) {
				return type;
			}
		}
		return null;
	}

	private final Class<? extends TimerMessage> classType;
	private final String className;

	private TimerMessageType(Class<? extends TimerMessage> clazzType) {
		this.classType = clazzType;

		this.className = this.classType.getSimpleName().toLowerCase();
	}

	public Class<? extends TimerMessage> getClassType() {
		return classType;
	}
}
