package de.ngloader.timer.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import de.ngloader.timer.api.TimerConfig;
import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.config.ConfigService;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.database.TimerDatabaseManager;
import de.ngloader.timer.api.i18n.TimerConfigTranslation;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.core.command.ImplCommandManager;
import de.ngloader.timer.core.config.ImplConfigService;
import de.ngloader.timer.core.database.ImplTimerDatabaseManager;
import de.ngloader.timer.core.i18n.ImplTimerLanguage;
import de.ngloader.timer.core.timer.ImplTimerManager;

public abstract class ImplTimerPlugin extends TimerPlugin {

	private final ConfigService configService;
	private final TimerLanguageService languageService;
	private final TimerDatabaseManager databaseManager;
	private final TimerCommandManager commandManager;

	private final TimerManager defaultManager = new ImplTimerManager();
	private final Set<TimerManager> managers = new HashSet<>();

	public ImplTimerPlugin() {
		TimerPlugin.setPlugin(this);

		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_LOADING);

		this.configService = new ImplConfigService(this);
		this.languageService = new ImplTimerLanguage(this);
		this.databaseManager = new ImplTimerDatabaseManager(this);
		this.commandManager = new ImplCommandManager(this);

		this.addManager(this.defaultManager);

		this.languageService.load();
		this.databaseManager.setDatabase(this.configService.getConfig(TimerConfig.class).databaseType);

		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_LOADING_TIMER);
		this.databaseManager.getDatabase().getTimer().forEach(this.getDefaultManager()::addTimer);
		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_LOADED_TIMER, this.defaultManager.getTimers().size());

		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_LOADED);
	}

	public void disable() {
		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_DISABLING);

		if (this.databaseManager != null && this.databaseManager.getDatabase() != null) {
			TimerDatabase database = this.databaseManager.getDatabase();

			if (this.defaultManager != null) {
				this.defaultManager.getTimers().forEach(database::updateTimer);
			}

			database.closeConnection();
		}

		this.log(TimerModule.MODULE_CORE, TimerMessage.CORE_DISABLED);
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
	public void deleteTimer(Timer timer) {
		this.managers.forEach(manager -> manager.removeTimer(timer));
	}

	@Override
	public void log(TimerModule module, TimerMessage message, Object... args) {
		if (this.languageService == null) {
			this.log(String.format("%s%s %s", TimerConfigTranslation.PREFIX, module.getMessage(), message.getMessage()));
			return;
		}

		this.log(String.format("%s%s %s",
				this.languageService.getPrefix(),
				this.languageService.translate(module),
				this.languageService.translate(message, args)));
	}

	@Override
	public TimerManager getDefaultManager() {
		return this.defaultManager;
	}

	@Override
	public TimerDatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}

	@Override
	public ConfigService getConfigService() {
		return this.configService;
	}

	@Override
	public TimerCommandManager getCommandManager() {
		return this.commandManager;
	}

	@Override
	public TimerLanguageService getLanguageService() {
		return this.languageService;
	}
}