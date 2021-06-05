package de.ngloader.timer.core.timer.action;

import java.util.function.Consumer;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerTickable;
import de.ngloader.timer.api.timer.action.TimerAction;

public abstract class ImplTimerAction implements TimerAction, TimerTickable {

	private final Timer timer;

	public ImplTimerAction(Timer timer) {
		this.timer = timer;
	}

	@Override
	public void run() { }

	@Override
	public void onCommand(Consumer<String> response, Consumer<String> permissionm, String[] args) { }

	@Override
	public void onCreate(Consumer<String> response) { }

	@Override
	public void onInfo(Consumer<String> response) { }

	@Override
	public void onHelp(Consumer<String> response) { }

	@Override
	public void onError(String error) { }

	@Override
	public void enable() { }

	@Override
	public void disable() { }

	@Override
	public Timer getTimer() {
		return this.timer;
	}

}
