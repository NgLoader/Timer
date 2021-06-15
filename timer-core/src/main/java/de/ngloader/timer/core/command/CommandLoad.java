package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

public class CommandLoad {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("load")
			.requires(info -> info.hasPermission("timer.load"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
