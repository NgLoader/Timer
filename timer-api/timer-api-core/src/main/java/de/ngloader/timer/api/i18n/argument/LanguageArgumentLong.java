package de.ngloader.timer.api.i18n.argument;

import de.ngloader.timer.api.i18n.Language;

public final class LanguageArgumentLong extends LanguageArgument {

	LanguageArgumentLong(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return language.numberFormat().format(this.value);
	}
}