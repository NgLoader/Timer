package de.ngloader.timer.api.command;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.database.TimerDatabase;
import de.ngloader.timer.api.i18n.argument.LanguageArgument;
import de.ngloader.timer.core.TimerMessageOLD;

public interface TimerCommandInfo {

	public TimerPlugin getPlugin();

	public Predicate<String> hasPermission();

	/**
	 * Return a list with all failed permission for the current request
	 * 
	 * @return failed permissions
	 */
	public Set<String> failedPermissionCheck();

	/**
	 * Return true when the permission is true
	 * 
	 * @param permission
	 * @return has permission
	 */
	public default boolean hasPermission(String permission) {
		return this.hasPermission().test(permission);	
	}

	/**
	 * Return true when all permissions are true
	 * 
	 * @param permissions
	 * @return has permission
	 */
	public default boolean hasPermission(String... permissions) {
		for (String permission : permissions) {
			if (!this.hasPermission().test(permission)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return true when one permission is true
	 * 
	 * @param permissions
	 * @return has permission
	 */
	public default boolean hasAnyPermission(String... permissions) {
		for (String permission : permissions) {
			if (this.hasPermission().test(permission)) {
				return true;
			}
		}
		return false;
	}

	public BiConsumer<String, LanguageArgument[]> response();

	/**
	 * Response with a translated message
	 * 
	 * @param message
	 * @param args
	 */
	public default void response(String message, LanguageArgument... args) {
		this.response().accept(message, args);
	}

	/**
	 * Receive a translated message
	 * 
	 * @param message
	 * @param args
	 * @return Translated message
	 */
	public default String translate(String message, LanguageArgument... args) {
		return this.getPlugin().getLanguageService().translate(message, args);
	}

	/**
	 * Checking if the database connection is open
	 * 
	 * @param send a not connected to database message
	 * @return true when the database connection is open
	 */
	public default boolean isDatabaseConnected(boolean sendNotConnectedMessage) {
		TimerDatabase database = this.getPlugin().getDatabaseManager().getDatabase();
		if (database != null && database.isConnected()) {
			return true;
		}

		if (sendNotConnectedMessage) {
			this.response(TimerMessageOLD.COMMAND_DATABASE_IS_NOT_CONNECTED);
		}
		return false;
	}
}