package de.ngloader.timer.core.util;

import java.util.regex.Pattern;

public enum RegexUtil {

	INTEGER("(?<=\\s|^)\\d+(?=\\s|$)"),
	NUMBER("^[0-9]+(\\.[0-9]+)?$"),
	BOOLEAN("^(true|false|1|0)$"),

	COLOR_HEX_PATTERN("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"),
	COLOR_CODE("[0-9]{1,3}");

	private final Pattern pattern;
	private final String regex;

	private RegexUtil(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(this.regex);
	}

	public boolean matches(String string) {
		return string.matches(this.regex);
	}

	public boolean matches(CharSequence string) {
		return this.pattern.matcher(string).find();
	}

	public Pattern getPattern() {
		return this.pattern;
	}

	public String getRegex() {
		return this.regex;
	}
}