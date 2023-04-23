package de.ngloader.timer.api.i18n;

import de.ngloader.timer.api.i18n.argument.LanguageArgument;

public interface LanguageService {

	String translate(String key, LanguageArgument... args);
}