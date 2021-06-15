package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

public class CommandEdit {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("edit")
			.requires(info -> info.hasPermission("timer.edit"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
