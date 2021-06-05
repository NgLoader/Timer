package de.ngloader.timer.api.timer.action;

import java.util.function.Consumer;

import de.ngloader.timer.api.timer.TimerType;

public interface TimerAction extends TimerType<TimerAction> {

	public void onCommand(Consumer<String> response, Consumer<String> permissionm, String[] args);
	public void onCreate(Consumer<String> response);
	public void onInfo(Consumer<String> response);
	public void onHelp(Consumer<String> response);
	public void onError(String error);
}