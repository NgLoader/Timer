package de.ngloader.timer.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.config.ConfigService;
import de.ngloader.timer.api.database.TimerDatabaseManager;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.core.config.ImplConfigService;
import de.ngloader.timer.core.database.ImplTimerDatabaseManager;
import de.ngloader.timer.core.timer.ImplTimerManager;

public abstract class ImplTimerPlugin extends TimerPlugin {

	private final ConfigService configService;
	private final TimerDatabaseManager databaseManager;

	private final TimerManager defaultManager = new ImplTimerManager();
	private final Set<TimerManager> managers = new HashSet<>();

	public ImplTimerPlugin() {
		TimerPlugin.setPlugin(this);

		this.log("Core", "Loading§8...");
		this.configService = new ImplConfigService(this);
		this.databaseManager = new ImplTimerDatabaseManager(this);

		this.addManager(this.defaultManager);

		this.log("Core", "Loading timers§8...");
		this.databaseManager.getDatabase().getTimer().forEach(this.getDefaultManager()::addTimer);
		this.log("Core", "Loaded §8" + this.defaultManager.getTimers().size() + " §7timer§8.");
		this.log("Core", "Loaded§8.");
	}

	@Override
	public UUID generateUUID() {
		UUID uuid;
		do {
			uuid = UUID.randomUUID();
		} while(this.existTimer(uuid));
		return uuid;
	}

	private boolean existTimer(UUID uuid) {
		for (TimerManager manager : this.managers) {
			for (Timer timer : manager.getTimers()) {
				if (timer.getId().equals(uuid)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean addManager(TimerManager manager) {
		Objects.requireNonNull(manager, "TimerManager is null");

		return this.managers.add(manager);
	}

	@Override
	public boolean removeManager(TimerManager manager) {
		return this.managers.remove(manager);
	}

	@Override
	public Set<TimerManager> getManagers() {
		return Collections.unmodifiableSet(this.managers);
	}

	@Override
	public TimerManager getDefaultManager() {
		return this.defaultManager;
	}

	@Override
	public void deleteTimer(Timer timer) {
		this.managers.forEach(manager -> manager.removeTimer(timer));
	}

	@Override
	public TimerDatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}

	@Override
	public ConfigService getConfigService() {
		return this.configService;
	}
}