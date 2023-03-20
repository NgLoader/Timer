package de.ngloader.timer.api;

import java.util.ArrayList;
import java.util.List;

public enum TimeMapper {

	TICK	(
			20,
			1),
	SECOND	(
			60,
			20),
	MINUTE	(
			60,
			20 * 60),
	HOUR	(
			24,
			20 * 60 * 60),
	DAY		(
			7,
			20 * 60 * 60 * 24),
	WEEK	(
			4,
			20 * 60 * 60 * 24 * 7),
	MONTH	(
			12,
			20 * 60 * 60 * 24 * 7 * 4),
	YEAR	(
			365,
			20 * 60 * 60 * 24 * 7 * 4 * 12);

	private static final String DEFAULT_SPLITTER = " ";

	public static String toPrettyString(long ticks) {
		return toPrettyString(ticks, DEFAULT_SPLITTER);
	}

	public static String toPrettyString(long ticks, String splitter) {
		StringBuilder builder = new StringBuilder();
		for (TimeMapper time : values()) {
			appendWhenValid(builder, time.name().toLowerCase(), time.toPartFromTicks(ticks), splitter);
		}
		return builder.toString();
	}

	private static void appendWhenValid(StringBuilder builder, String timeName, long number, String splitter) {
		if (number != 0) {
			if (!builder.isEmpty()) {
				builder.append(splitter);
			}
			builder.append(number);
			builder.append(timeName);
		}
	}

	public static TimeMapper suggestFirst(String input) {
		input = input.toLowerCase();

		for (TimeMapper time : values()) {
			if (time.name().toLowerCase().startsWith(input)) {
				return time;
			}
		}
		return null;
	}

	public static List<TimeMapper> suggest(String input) {
		input = input.toLowerCase();

		List<TimeMapper> result = new ArrayList<>();
		for (TimeMapper time : values()) {
			if (time.name().toLowerCase().startsWith(input)) {
				result.add(time);
			}
		}
		return result;
	}

	private final long part;
	private final long ticks;

	private TimeMapper(long part, long inTicks) {
		this.part = part;
		this.ticks = inTicks;
	}

	public long toPartFromTicks(long ticks) {
		return (ticks / this.ticks) % this.part;
	}

	public long getTicks() {
		return this.ticks;
	}
}
