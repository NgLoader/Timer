package de.ngloader.timer.api;

import de.ngloader.timer.api.timer.Timer;

public interface TimerPlugin {

	public void deleteTimer(Timer timer);

	public void sendChatMessage(String message, String permission);
	public void sendChatCommand(String message, String permission);
	public void sendConsoleCommand(String message);
}
