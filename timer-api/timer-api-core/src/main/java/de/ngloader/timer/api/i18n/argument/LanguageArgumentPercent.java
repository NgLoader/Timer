package de.ngloader.timer.api.i18n.argument;

import de.ngloader.timer.api.i18n.Language;

public final class LanguageArgumentPercent extends LanguageArgument {

	LanguageArgumentPercent(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return language.percentFormat().format(this.value);
	}
}