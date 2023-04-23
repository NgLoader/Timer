package de.ngloader.timer.api.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import de.ngloader.timer.api.i18n.argument.LanguageArgument;

public record Language(Locale locale, ResourceBundle bundle, DateFormat dateFormat, NumberFormat numberFormat, PercentFormat percentFormat) {

	public String format(String key, LanguageArgument... args) {
		if (!this.bundle.containsKey(key)) {
			return key;
		}

		String message = this.bundle.getString(key);
		char[] array = message.toCharArray();

		StringBuilder builder = new StringBuilder();
		int step = 0;
		for (int i = 0; i < array.length; i++) {
			char letter = array[i];

			if (array[i] == '{' && array.length > i + 1 && array[i + 1] == '}') {
				i++;

				if (args.length > step) {
					builder.append(args[step++].format(this));
				}
				continue;
			}

			builder.append(letter);
		}

		return builder.toString();
	}
}
