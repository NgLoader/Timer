package de.ngloader.timer.api.timer.message;

import de.ngloader.timer.api.timer.TimerType;

public interface TimerMessage extends TimerType<TimerMessage> {

	public long getTick();
	public void setTick(long tick);

	public String getContent();
	public String getUncoloredContent();
	public void setContent(String content);

	public String getPermission();
	public void setPermission(String permission);
}