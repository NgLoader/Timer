package de.ngloader.timer.core.util;

import java.util.List;
import java.util.stream.Stream;

public class StringUtil {

	public static String toFirstUpper(String input) {
		return input.length() > 1 ? input.substring(0, 1).toUpperCase() + input.substring(1) : input.toUpperCase();
	}

	public static List<String> enumNamesAsList(Class<? extends Enum<?>> enumClass) {
		return enumNames(enumClass).toList();
	}

	public static String[] enumNamesAsArray(Class<? extends Enum<?>> enumClass) {
		return enumNames(enumClass).toArray(String[]::new);
	}

	public static Stream<String> enumNames(Class<? extends Enum<?>> enumClass) {
		return Stream.of(enumClass.getEnumConstants()).map(value -> value.name());
	}
}