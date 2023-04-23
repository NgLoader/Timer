package de.ngloader.timer.api.i18n.argument;

import de.ngloader.timer.api.i18n.Language;

public final class LanguageArgumentObject extends LanguageArgument {

	LanguageArgumentObject(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return this.value.toString();
	}
}