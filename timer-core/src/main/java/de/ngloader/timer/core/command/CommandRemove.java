package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

@TimerCommand(aliases = { "delete", "del", "rm" })
public class CommandRemove {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("remove")
			.requires(info -> info.hasPermission("timer.remove"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
