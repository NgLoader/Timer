package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

public class CommandSave {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("save")
			.requires(info -> info.hasPermission("timer.save"))
			.executes(command -> {
				return TimerCommandResponse.HELP;
			});
}
