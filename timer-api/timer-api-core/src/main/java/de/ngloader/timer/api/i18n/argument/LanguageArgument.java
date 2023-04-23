package de.ngloader.timer.api.i18n.argument;

import java.util.Date;

import de.ngloader.timer.api.i18n.Language;

public abstract sealed class LanguageArgument permits LanguageArgumentDate, LanguageArgumentLong, LanguageArgumentObject, LanguageArgumentPercent {

	public static LanguageArgument toLong(long number) {
		return new LanguageArgumentLong(number);
	}

	public static LanguageArgument toDouble(double number) {
		return new LanguageArgumentLong(number);
	}

	public static LanguageArgument toPercent(int number) {
		return LanguageArgument.toPercent((double) 1 / 100 * number);
	}

	public static LanguageArgument toPercent(long number) {
		return new LanguageArgumentPercent(number);
	}

	public static LanguageArgument toPercent(double number) {
		return new LanguageArgumentPercent(number);
	}

	public static LanguageArgument toDate(long date) {
		return LanguageArgument.toDate(new Date(date));
	}

	public static LanguageArgument toDate(Date date) {
		return new LanguageArgumentDate(date);
	}

	public static LanguageArgument toObject(Object object) {
		return new LanguageArgumentObject(object);
	}

	protected final Object value;

	LanguageArgument(Object value) {
		this.value = value;
	}

	public abstract String format(Language language);
}