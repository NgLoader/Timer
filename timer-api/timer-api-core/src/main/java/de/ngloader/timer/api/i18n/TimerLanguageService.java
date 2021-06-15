package de.ngloader.timer.api.i18n;

import de.ngloader.timer.api.TimerPlugin;

public interface TimerLanguageService {

	public void load();

	public String getPrefix();
	public String translate(TimerModule module);
	public String translate(TimerMessage message, Object... args);

	public TimerPlugin getPlugin();
}