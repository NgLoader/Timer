package de.ngloader.timer.api;

import java.util.Set;
import java.util.UUID;

import de.ngloader.timer.api.config.ConfigService;
import de.ngloader.timer.api.database.TimerDatabaseManager;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;

//protected static functions implemented in java 9 but we use java 8
public abstract class TimerPlugin {

	private static TimerPlugin plugin;

	public static TimerPlugin getPlugin() {
		return TimerPlugin.plugin;
	}

	protected static void setPlugin(TimerPlugin plugin) {
		if (TimerPlugin.plugin != null) {
			throw new IllegalArgumentException("TimerPlugin was already initialized");
		}

		TimerPlugin.plugin = plugin;
	}

	public abstract ConfigService getConfigService();
	public abstract TimerDatabaseManager getDatabaseManager();

	public abstract boolean addManager(TimerManager manager);
	public abstract boolean removeManager(TimerManager manager);
	public abstract Set<TimerManager> getManagers();

	public abstract TimerManager getDefaultManager();

	public abstract UUID generateUUID();

	public abstract void deleteTimer(Timer timer);

	public abstract void sendChatMessage(String message, String permission);
	public abstract void sendChatCommand(String message, String permission);
	public abstract void sendConsoleCommand(String message);

	public abstract void log(String module, String message);
	public abstract void logError(String module, String message, Throwable... throwable);
}