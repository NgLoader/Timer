package de.ngloader.timer.api.i18n.argument;

import de.ngloader.timer.api.i18n.Language;

public final class LanguageArgumentDate extends LanguageArgument {

	LanguageArgumentDate(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return language.dateFormat().format(this.value);
	}
}