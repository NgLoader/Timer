package de.ngloader.timer.core.timer;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerTickable;
import de.ngloader.timer.api.timer.TimerType;
import de.ngloader.timer.api.timer.action.TimerAction;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.api.timer.sort.TimerSort;
import de.ngloader.timer.api.timer.stop.TimerStop;

public class ImplTimer implements Timer {

	private final TimerPlugin plugin;
	private final UUID id;

	private String name;
	private boolean enabled = false;

	private TimerAction actionType;
	private TimerStop stopType;
	private TimerSort sortType;

	private List<TimerMessage> messages = new LinkedList<>();
	private Set<TimerTickable> tickables = Collections.synchronizedSet(new HashSet<>());

	private long currentTick;
	private long maxTick;

	public ImplTimer(TimerPlugin plugin, UUID id, String name, boolean enabled, TimerAction actionType, TimerStop stopType) {
		this.plugin = plugin;
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.actionType = actionType;
		this.stopType = stopType;
	}

	@Override
	public void run() {
		if (this.enabled) {
			synchronized (this.tickables) {
				this.tickables.forEach(type -> type.onTick(this.currentTick));
			}

			if (this.currentTick == this.maxTick) {
				this.actionType.run();
				this.stopType.run();
			}

			this.currentTick++;
		}
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (this.enabled) {
			this.stopType.enable();
			this.sortType.enable();
			this.messages.forEach(TimerMessage::enable);
			this.actionType.enable();
		} else {
			this.stopType.disable();
			this.sortType.disable();
			this.messages.forEach(TimerMessage::disable);
			this.actionType.disable();
		}
	}

	@Override
	public TimerAction getActionType() {
		return this.actionType;
	}

	@Override
	public void setActionType(TimerAction type) {
		this.checkTickable(this.actionType, true);
		this.actionType = type;
		this.checkTickable(this.actionType, false);
	}

	@Override
	public TimerStop getStopType() {
		return this.stopType;
	}

	@Override
	public void setStopType(TimerStop type) {
		this.checkTickable(this.stopType, true);
		this.stopType = type;
		this.checkTickable(this.stopType, false);
	}

	@Override
	public TimerSort getSortType() {
		return this.sortType;
	}

	@Override
	public void setSortType(TimerSort type) {
		this.checkTickable(this.stopType, true);
		this.sortType = type;
		this.checkTickable(this.stopType, false);
	}

	@Override
	public List<TimerMessage> getMessages() {
		return Collections.unmodifiableList(this.messages);
	}

	@Override
	public TimerMessage getMessage(int index) {
		if (index > -1 && this.messages.size() > index) {
			return this.messages.get(index);
		}
		return null;
	}

	@Override
	public void moveMessage(int from, int to) {
		Collections.swap(this.messages, from, to);
	}

	@Override
	public boolean addMessage(TimerMessage message) {
		if (this.messages.contains(message)) {
			return false;
		}
		if (this.messages.add(message)) {
			this.checkTickable(message, false);
			return true;
		}
		return false;
	}

	@Override
	public void setMessages(List<TimerMessage> messages) {
		this.messages.clear();
		messages.stream().distinct().forEachOrdered(this::addMessage);
	}

	@Override
	public boolean deleteMessage(TimerMessage message) {
		this.checkTickable(message, true);
		return this.messages.remove(message);
	}

	@Override
	public long getCurrentTick() {
		return this.currentTick;
	}

	@Override
	public void setCurrentTick(long tick) {
		this.currentTick = tick;
	}

	@Override
	public long getMaxTick() {
		return this.maxTick;
	}

	@Override
	public void setMaxTick(long ticks) {
		this.maxTick = ticks;
	}

	@Override
	public Set<TimerTickable> getTickables() {
		synchronized (this.tickables) {
			return Collections.unmodifiableSet(this.tickables);
		}
	}

	@Override
	public void setTickables(Set<TimerTickable> tickables) {
		synchronized (this.tickables) {
			this.tickables.clear();
			this.tickables.addAll(tickables);
		}
	}

	@Override
	public boolean addTickable(TimerTickable tickable) {
		synchronized (this.tickables) {
			return this.tickables.add(tickable);
		}
	}

	@Override
	public boolean removeTickable(TimerTickable tickable) {
		synchronized (this.tickables) {
			return this.tickables.remove(tickable);
		}
	}

	private void checkTickable(TimerType<?> type, boolean remove) {
		if (type instanceof TimerTickable) {
			if (remove) {
				this.removeTickable((TimerTickable) type);
			} else {
				this.addTickable((TimerTickable) type);
			}
		}
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}