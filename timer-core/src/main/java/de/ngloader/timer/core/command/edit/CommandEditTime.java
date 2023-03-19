package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.ArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.core.util.SuggestionUtil;

public class CommandEditTime {

	public static final ArgumentBuilder<TimerCommandInfo, ?> COMMAND = literal("time")
			.then(literal("current")
					.then(argument("time", string())
							.executes(context -> {
								return 0;
							})
							.suggests(SuggestionUtil.SUGGEST_TIME_INPUT)))
			.then(literal("max"));

	public static int handle(TimerCommandInfo commandInfo, String command) {
		return TimerCommandResponse.OK;
	}
}
