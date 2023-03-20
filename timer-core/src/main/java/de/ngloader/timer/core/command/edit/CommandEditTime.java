package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.ArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.core.util.SuggestionUtil;

public class CommandEditTime {

	public static final ArgumentBuilder<TimerCommandInfo, ?> COMMAND = literal("time")
			.then(literal("current")
					.then(argument("time", string())
							.executes(context -> {
								Timer timer = CommandEdit.getTimer(context, true);
								if (timer == null) {
									return TimerCommandResponse.OK;
								}

								Long time = SuggestionUtil.suggestTimeInput(getString(context, "time"));
								if (time == null) {
									return TimerCommandResponse.SYNTAX;
								}
								return handleCurrent(context.getSource(), timer, time);
							})
							.suggests(SuggestionUtil.SUGGEST_TIME_INPUT)))
			.then(literal("max")
					.then(argument("time", string())
							.executes(context -> {
								Timer timer = CommandEdit.getTimer(context, true);
								if (timer == null) {
									return TimerCommandResponse.OK;
								}

								Long time = SuggestionUtil.suggestTimeInput(getString(context, "time"));
								if (time == null) {
									return TimerCommandResponse.SYNTAX;
								}
								return handleMax(context.getSource(), timer, time);
							})
							.suggests(SuggestionUtil.SUGGEST_TIME_INPUT)));

	public static int handleCurrent(TimerCommandInfo commandInfo, Timer timer, long time) {
		timer.setCurrentTick(time);
//		System.out.println(TimeMapper.toPrettyString(time));
//		commandInfo.response(TimerMessage., null);
		return TimerCommandResponse.OK;
	}

	public static int handleMax(TimerCommandInfo commandInfo, Timer timer, long time) {
		timer.setMaxTick(time);
		return TimerCommandResponse.OK;
	}
}
