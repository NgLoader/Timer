package de.ngloader.timer.core.timer;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.api.timer.TimerTickable;
import de.ngloader.timer.api.timer.TimerType;
import de.ngloader.timer.api.timer.action.TimerAction;
import de.ngloader.timer.api.timer.message.TimerMessage;
import de.ngloader.timer.api.timer.sort.TimerSort;
import de.ngloader.timer.api.timer.stop.TimerStop;

public class ImplTimer implements Timer {

	private final TimerPlugin plugin;
	private final UUID id;

	private TimerManager timerManager;

	private String name;
	private boolean enabled = false;

	private TimerAction actionType;
	private TimerStop stopType;
	private TimerSort sortType;

	private List<TimerMessage> messages = new LinkedList<>();
	private Set<TimerTickable> tickables = new HashSet<>();

	private long currentTick;
	private long maxTick;

	public ImplTimer(TimerPlugin plugin, TimerManager timerManager, UUID id, String name, boolean enabled) {
		this.plugin = plugin;
		this.timerManager = timerManager;
		this.id = id;
		this.name = name;
		this.enabled = enabled;
	}

	@Override
	public void run() {
		this.tickables.forEach(type -> type.onTick(this.currentTick));

		if (this.currentTick == this.maxTick) {
			this.typeAction(TimerType::run, this.actionType, this.stopType);
		}

		this.currentTick++;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public TimerManager getManager() {
		return this.timerManager;
	}

	@Override
	public void setManager(TimerManager manager) {
		Objects.requireNonNull(manager, "TimerManager is null");

		if (this.timerManager != null) {
			this.timerManager.removeTimer(this);
		}

		this.timerManager = manager;
		this.timerManager.addTimer(this);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		Objects.requireNonNull(name, "Name is null");

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
			this.typeAction(TimerType::enable, this.stopType, this.sortType);
			this.messages.forEach(TimerMessage::enable);
			this.typeAction(TimerType::enable, this.actionType);
		} else {
			this.typeAction(TimerType::disable, this.actionType);
			this.messages.forEach(TimerMessage::disable);
			this.typeAction(TimerType::disable, this.sortType, this.stopType);
		}
	}

	@Override
	public TimerAction getActionType() {
		return this.actionType;
	}

	@Override
	public void setActionType(TimerAction type) {
		Objects.requireNonNull(type, "TimerAction is null");

		this.actionType = this.switchAction(this.actionType, type);
	}

	@Override
	public TimerStop getStopType() {
		return this.stopType;
	}

	@Override
	public void setStopType(TimerStop type) {
		Objects.requireNonNull(type, "TimerStop is null");

		this.stopType = this.switchAction(this.stopType, type);
	}

	@Override
	public TimerSort getSortType() {
		return this.sortType;
	}

	@Override
	public void setSortType(TimerSort type) {
		Objects.requireNonNull(type, "TimerSort is null");

		this.sortType = this.switchAction(this.sortType, type);
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
		Objects.requireNonNull(message, "TimerMessage is null");

		if (this.messages.contains(message)) {
			return false;
		} else if (this.messages.add(message)) {
			this.switchAction(null, message);
			return true;
		}
		return false;
	}

	@Override
	public void setMessages(List<TimerMessage> messages) {
		Objects.requireNonNull(messages, "Messages is null");

		this.messages.forEach(message -> this.switchAction(message, null));
		this.messages.clear();
		messages.stream().distinct().forEachOrdered(this::addMessage);
	}

	@Override
	public boolean deleteMessage(TimerMessage message) {
		Objects.requireNonNull(message, "TimerMessage is null");

		this.switchAction(message, null);
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
		return Collections.unmodifiableSet(this.tickables);
	}

	@Override
	public void setTickables(Set<TimerTickable> tickables) {
		Objects.requireNonNull(tickables, "Tickables is null");

		this.tickables.clear();
		this.tickables.addAll(tickables);
	}

	@Override
	public boolean addTickable(TimerTickable tickable) {
		return this.tickables.add(tickable);
	}

	@Override
	public boolean removeTickable(TimerTickable tickable) {
		return this.tickables.remove(tickable);
	}

	private void updateTickable(TimerType<?> type, boolean remove) {
		if (type != null && type instanceof TimerTickable) {
			if (remove) {
				this.removeTickable((TimerTickable) type);
			} else {
				this.addTickable((TimerTickable) type);
			}
		}
	}

	private void typeAction(Consumer<TimerType<?>> action, TimerType<?>... types) {
		for (TimerType<?> type : types) {
			if (type != null) {
				action.accept(type);
			}
		}
	}

	private <T extends TimerType<?>> T switchAction(T before, T after) {
		this.updateTickable(before, true);
		if (this.enabled) {
			this.typeAction(TimerType::disable, before);
			this.typeAction(TimerType::enable, after);
		} else {
			this.typeAction(TimerType::disable, after);
		}
		this.updateTickable(after, false);
		return after;
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}