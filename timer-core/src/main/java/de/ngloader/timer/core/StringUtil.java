package de.ngloader.timer.core;

public class StringUtil {

	public static String toFirstUpper(String input) {
		return input.length() > 1 ? input.substring(0, 1).toUpperCase() + input.substring(1) : input.toUpperCase();
	}
}