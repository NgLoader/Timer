package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

@TimerCommand(aliases = { "cmd" })
public class CommandCmd {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("command")
			.requires(info -> info.hasPermission("timer.cmd"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
