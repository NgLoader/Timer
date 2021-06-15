package de.ngloader.timer.api.i18n;

import java.util.HashMap;
import java.util.Map;

public enum TimerModule {

	MODULE_CORE("§8[§eCore§8]"),
	MODULE_I18N("§8[§eI18n§8]"),
	MODULE_DATABASE("§8[§eDatabase§8]"),
	MODULE_CONFIG_SERVICE("§8[§eConfigService§8]"),
	MODULE_COMMAND("§8[§eCommand§8]");

	public static Map<TimerModule, String> toMap() {
		Map<TimerModule, String> messages = new HashMap<>();
		for (TimerModule message : values()) {
			messages.put(message, message.getMessage());
		}
		return messages;
	}

	private final String message;

	private TimerModule(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}