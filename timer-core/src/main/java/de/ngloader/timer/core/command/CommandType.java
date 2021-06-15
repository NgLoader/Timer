package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

@TimerCommand(aliases = { "types" })
public class CommandType {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("type")
			.requires(info -> info.hasPermission("timer.type"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
