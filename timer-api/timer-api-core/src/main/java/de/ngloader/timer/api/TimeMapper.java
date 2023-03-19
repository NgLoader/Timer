package de.ngloader.timer.api;

import java.util.ArrayList;
import java.util.List;

public enum TimeMapper {

	TICK	(1),
	SECOND	(20),
	MINUTE	(20 * 60),
	HOUR	(20 * 60 * 60),
	DAY		(20 * 60 * 60 * 24),
	WEEK	(20 * 60 * 60 * 24 * 7),
	MONTH	(20 * 60 * 60 * 24 * 7 * 4),
	YEAR	(20 * 60 * 60 * 24 * 7 * 4 * 12);

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

	private final long ticks;

	private TimeMapper(long inTicks) {
		this.ticks = inTicks;
	}

	public long getTicks() {
		return this.ticks;
	}
}
