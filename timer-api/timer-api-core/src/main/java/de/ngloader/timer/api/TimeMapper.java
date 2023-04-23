package de.ngloader.timer.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TimeMapper {

	TICK	(
			20, // Tick
			1), // In ticks
	SECOND	(
			60, 	// Second
			20), 	// In ticks
	MINUTE	(
			60, 		// Minute
			20 * 60), 	// In ticks
	HOUR	(
			24, 			// Hour
			20 * 60 * 60), 	// In ticks
	DAY 	(
			7, 					// Day
			20 * 60 * 60 * 24), // In ticks
	WEEK	(
			4, 						// Week
			20 * 60 * 60 * 24 * 7), // In ticks
	MONTH	(
			12, 						// Month
			20 * 60 * 60 * 24 * 7 * 4), // In ticks
	YEAR	(
			365, 								// Year
			20 * 60 * 60 * 24 * 7 * 4 * 12); 	// In ticks

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

		List<TimeMapper> result = null;
		for (TimeMapper time : values()) {
			if (time.name().toLowerCase().startsWith(input)) {
				if (result == null) {
					result = new ArrayList<>();
				}
				result.add(time);
			}
		}
		return result != null ? result : Collections.emptyList();
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
