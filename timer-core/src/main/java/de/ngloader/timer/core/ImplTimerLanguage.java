package de.ngloader.timer.core;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.i18n.TimerConfigTranslation;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.i18n.TimerModule;

public class ImplTimerLanguage implements TimerLanguageService {

	private final TimerPlugin plugin;

	private String prefix;
	private Map<TimerModule, String> modules = new HashMap<>();
	private Map<TimerMessage, String> messages = new HashMap<>();

	public ImplTimerLanguage(TimerPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void load() {
		this.plugin.log(TimerModule.MODULE_I18N, TimerMessage.CORE_LOADING_TRANSLATION);

		TimerConfigTranslation config = this.plugin.getConfigService().getConfig(TimerConfigTranslation.class);
		String prefix = config.prefix;
		Map<TimerModule, String> modules = config.modules != null ? new HashMap<>(config.modules) : new HashMap<>();
		Map<TimerMessage, String> messages = config.messages != null ? new HashMap<>(config.messages) : new HashMap<>();

		boolean update = false;
		if (prefix == null) {
			prefix = TimerConfigTranslation.PREFIX;
			config.prefix = prefix;

			update = true;
		}
		if (this.loadMessages(modules, TimerModule.values(), (module -> module.getMessage()))) {
			config.modules = modules;
	
			update = true;
		}
		if (this.loadMessages(messages, TimerMessage.values(), (module -> module.getMessage()))) {
			config.messages = messages;
	
			update = true;
		}

		if (update) {
			this.plugin.getConfigService().saveConfig(config);
		}

		this.prefix = prefix;
		this.modules = modules;
		this.messages = messages;

		this.plugin.log(TimerModule.MODULE_I18N, TimerMessage.CORE_LOADED_TRANSLATION, this.messages.size());
	}

	private <T extends Enum<T>> boolean loadMessages(Map<T, String> messages, T[] values, Function<T, String> translate) {
		boolean update = false;
		for (T message : values) {
			if (!messages.containsKey(message)) {
				messages.put(message, translate.apply(message));
				update = true;
			}
		}
		return update;
	}

	@Override
	public String getPrefix() {
		return this.prefix != null ? this.prefix.replace("&", "§") : TimerConfigTranslation.PREFIX;
	}

	@Override
	public String translate(TimerModule module) {
		Objects.requireNonNull(module, "Translation key is null");

		return this.modules.getOrDefault(module, module.getMessage()).replace("&", "§");
	}

	@Override
	public String translate(TimerMessage message, Object... args) {
		Objects.requireNonNull(message, "Translation key is null");

		return MessageFormat.format(this.messages.getOrDefault(message, message.getMessage()).replace("&", "§"), args);
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}