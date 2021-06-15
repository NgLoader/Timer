package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

@TimerCommand(aliases = { "rl" })
public class CommandReload {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("reload")
			.requires(info -> info.hasPermission("timer.reload"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
