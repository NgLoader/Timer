package de.ngloader.timer.core;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

import de.ngloader.timer.api.GeneralConfig;
import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.api.i18n.TimerModule;

public class ImplTimerLanguage2 implements TimerLanguageService {

	private final TimerPlugin plugin;

	private final Locale locale;

	private final DateFormat dateFormat;
	private final NumberFormat numberFormat;
	private final String percentFormat = "\"{0,number,#.##%}\"";

	public ImplTimerLanguage2(TimerPlugin plugin) {
		this.plugin = plugin;

		GeneralConfig generalConfig = this.plugin.getConfigService().getConfig(GeneralConfig.class);
		this.locale = Locale.forLanguageTag(generalConfig.locale);
		this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, this.locale);
		this.numberFormat = NumberFormat.getNumberInstance(this.locale);
	}

	@Override
	public void load() {
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translate(TimerModule module) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translate(TimerMessageOLD message, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}