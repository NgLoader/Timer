package de.ngloader.timer.core.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.core.TimerMessageOLD;

public class ImplTimerCommandInfo implements TimerCommandInfo {

//	public ImplTimerCommandInfo(TimerPlugin plugin, Predicate<String> permission, Consumer<String> response) {
//	}

	private final TimerPlugin plugin;

	private Predicate<String> permission;
	private Set<String> failedPermission;

	private Consumer<String> response;

	public ImplTimerCommandInfo(TimerPlugin plugin, Predicate<String> permission, Consumer<String> response) {
		this.plugin = plugin;
		this.response = response;

		this.permission = permission;
	}

	private boolean captureFailedPermission(String permission) {
		if (this.permission.test(permission)) {
			return true;
		}

		if (this.failedPermission == null) {
			this.failedPermission = new HashSet<>();
		}
		this.failedPermission.add(permission);
		return false;
	}

	@Override
	public Predicate<String> hasPermission() {
		return this::captureFailedPermission;
	}

	@Override
	public Set<String> failedPermissionCheck() {
		return this.failedPermission != null ? this.failedPermission : Collections.emptySet();
	}

	@Override
	public BiConsumer<TimerMessageOLD, Object[]> response() {
		if (this.response == null) {
			return null;
		}

		TimerLanguageService languageService = this.plugin.getLanguageService();
		return (message, args) -> this.response.accept(languageService.getPrefix() + languageService.translate(message, args));
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}
