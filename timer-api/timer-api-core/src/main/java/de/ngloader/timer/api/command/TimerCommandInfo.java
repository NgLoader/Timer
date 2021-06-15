package de.ngloader.timer.api.command;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import de.ngloader.timer.api.i18n.TimerMessage;

public interface TimerCommandInfo {

	public Predicate<String> hasPermission();
	public default boolean hasPermission(String permission) {
		return this.hasPermission().test(permission);
	}

	public BiConsumer<TimerMessage, Object[]> response();
	public default void response(TimerMessage message, Object... args) {
		this.response().accept(message, args);
	}
}