package de.ngloader.timer.core.timer.message;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.message.TimerMessage;

public class ImplTimerMessage implements TimerMessage {

	private final Timer timer;

	private long tick = 0;

	private String content = null;
	private String contentUncolored = null;

	private String permssion = null;

	public ImplTimerMessage(Timer timer) {
		this.timer = timer;
	}

	public ImplTimerMessage(Timer timer, long tick, String content, String permission) {
		this(timer);

		this.setTick(tick);
		this.setContent(content);
		this.setPermission(permission);
	}

	@Override
	public void run() { }

	@Override
	public void enable() { }

	@Override
	public void disable() { }

	@Override
	public long getTick() {
		return this.tick;
	}

	@Override
	public void setTick(long tick) {
		this.tick = tick;
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public String getUncoloredContent() {
		return this.contentUncolored;
	}

	@Override
	public void setContent(String content) {
		this.content = content.replace("&", "&");
		this.contentUncolored = content.replace("§", "&");
	}

	@Override
	public String getPermission() {
		return this.permssion;
	}

	@Override
	public void setPermission(String permission) {
		this.permssion = permission;
	}

	@Override
	public Timer getTimer() {
		return this.timer;
	}
}