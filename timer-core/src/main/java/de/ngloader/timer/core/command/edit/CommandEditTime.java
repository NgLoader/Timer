package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.ArgumentBuilder;

import de.ngloader.timer.api.TimeMapper;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.core.TimerMessageOLD;
import de.ngloader.timer.core.util.SuggestionUtil;

public class CommandEditTime {

	public static final ArgumentBuilder<TimerCommandInfo, ?> COMMAND = literal("time")
			.then(literal("current")
					.requires(commandInfo -> commandInfo.hasPermission("timer.edit.time.current"))
					.then(argument("time", string())
							.executes(context -> {
								TimerCommandInfo commandInfo = context.getSource();
								Timer timer = CommandEdit.getTimer(context, true);
								if (timer == null) {
									return TimerCommandResponse.OK;
								}

								String permission = String.format("timer.edit.time.%s.current", timer.getName().toLowerCase());
								if (!commandInfo.hasAnyPermission("timer.edit.time.*.current", permission)) {
									return TimerCommandResponse.PERMISSION;
								}

								Long time = SuggestionUtil.getSuggestedTime(getString(context, "time"));
								if (time == null) {
									return TimerCommandResponse.SYNTAX;
								}
								return handleCurrent(commandInfo, timer, time);
							})
							.suggests(SuggestionUtil.SUGGEST_TIME_INPUT)))
			.then(literal("max")
					.requires(commandInfo -> commandInfo.hasPermission("timer.edit.time.max"))
					.then(argument("time", string())
							.executes(context -> {
								TimerCommandInfo commandInfo = context.getSource();
								Timer timer = CommandEdit.getTimer(context, true);
								if (timer == null) {
									return TimerCommandResponse.OK;
								}

								String permission = String.format("timer.edit.time.%s.max", timer.getName().toLowerCase());
								if (!commandInfo.hasAnyPermission("timer.edit.time.*.max", permission)) {
									return TimerCommandResponse.PERMISSION;
								}

								Long time = SuggestionUtil.getSuggestedTime(getString(context, "time"));
								if (time == null) {
									return TimerCommandResponse.SYNTAX;
								}
								return handleMax(commandInfo, timer, time);
							})
							.suggests(SuggestionUtil.SUGGEST_TIME_INPUT)));

	public static int handleCurrent(TimerCommandInfo commandInfo, Timer timer, long time) {
		timer.setCurrentTick(time);
		if (timer.getMaxTick() > time) {
			commandInfo.response(TimerMessageOLD.COMMAND_EDIT_INPUT_IS_HIGHER_THAN_MAX_TICK, TimeMapper.toPrettyString(time), TimeMapper.toPrettyString(timer.getMaxTick()));
		} else {
			timer.setCurrentTick(time);
			commandInfo.response(TimerMessageOLD.COMMAND_EDIT_NEW_CURRENT_TICK_SET, TimeMapper.toPrettyString(timer.getCurrentTick()));
		}

		return TimerCommandResponse.OK;
	}

	public static int handleMax(TimerCommandInfo commandInfo, Timer timer, long time) {
		timer.setMaxTick(time);
		commandInfo.response(TimerMessageOLD.COMMAND_EDIT_NEW_MAX_TICK_SET, TimeMapper.toPrettyString(timer.getMaxTick()));
		return TimerCommandResponse.OK;
	}
}
