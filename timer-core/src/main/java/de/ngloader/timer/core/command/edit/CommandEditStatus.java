package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.ArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

public class CommandEditStatus {

	public static final ArgumentBuilder<TimerCommandInfo, ?> COMMAND = literal("status")
			.requires(commandInfo -> commandInfo.hasPermission("timer.edit.status"));

	public static int handle(TimerCommandInfo commandInfo, String command) {
		return TimerCommandResponse.OK;
	}
}
