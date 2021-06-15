package de.ngloader.timer.api.i18n;

import java.util.Map;

import de.ngloader.timer.api.config.Config;

@Config(name = "messages")
public class TimerConfigTranslation {

	public static final String PREFIX = "§8[§aTimer§8] ";

	public String prefix = PREFIX;
	public Map<TimerModule, String> modules = TimerModule.toMap();
	public Map<TimerMessage, String> messages = TimerMessage.toMap();
}