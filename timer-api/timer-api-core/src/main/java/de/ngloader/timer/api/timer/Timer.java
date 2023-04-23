package de.ngloader.timer.api.timer;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.ngloader.timer.api.TimerLogger;
import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.timer.action.TimerAction;
import de.ngloader.timer.api.timer.action.TimerActionType;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.api.timer.message.TimerMessageType;
import de.ngloader.timer.api.timer.sort.TimerSort;
import de.ngloader.timer.api.timer.sort.TimerSortType;
import de.ngloader.timer.api.timer.stop.TimerStop;
import de.ngloader.timer.api.timer.stop.TimerStopType;

public interface Timer extends Runnable {

	public UUID getId();

	public TimerManager getManager();
	public void setManager(TimerManager manager);

	public String getName();
	public void setName(String name);

	public boolean isEnabled();
	public void setEnabled(boolean enabled);

	public TimerAction getActionType();
	public void setActionType(TimerAction type);
	public default void setActionType(TimerActionType type) {
		try {
			this.setActionType(TimerType.newInstance(this, type.getClassType()));
		} catch (Exception e) {
			TimerLogger.error("Error by execution command (setActionType)", e);
		}
	}

	public TimerStop getStopType();
	public void setStopType(TimerStop type);
	public default void setStopType(TimerStopType type) {
		try {
			this.setStopType(TimerType.newInstance(this, type.getClassType()));
		} catch (Exception e) {
			TimerLogger.error("Error by execution command (setStopType)", e);
		}
	}

	public TimerSort getSortType();
	public void setSortType(TimerSort type);
	public default void setSortType(TimerSortType type) {
		try {
			this.setSortType(TimerType.newInstance(this, type.getClassType()));
		} catch (Exception e) {
			TimerLogger.error("Error by execution command (setSortType)", e);
		}
	}

	public List<TimerMessage> getMessages();
	public TimerMessage getMessage(int index);
	public void moveMessage(int from, int to);
	public void setMessages(List<TimerMessage> messages);
	public boolean addMessage(TimerMessage message);
	public default void addMessage(TimerMessageType type, long tick, String content, String permission) throws Exception {
		TimerMessage message = TimerType.newInstance(this, type.getClassType());
		message.setTick(tick);
		message.setContent(content);
		message.setPermission(permission);
		this.addMessage(message);
	}
	public boolean deleteMessage(TimerMessage message);

	public long getCurrentTick();
	public void setCurrentTick(long tick);

	public long getMaxTick();
	public void setMaxTick(long ticks);

	public Set<TimerTickable> getTickables();
	public void setTickables(Set<TimerTickable> tickables);
	public boolean addTickable(TimerTickable tickable);
	public boolean removeTickable(TimerTickable tickable);

	public TimerPlugin getPlugin();
}