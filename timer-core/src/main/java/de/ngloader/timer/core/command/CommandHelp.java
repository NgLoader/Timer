package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.*;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

@TimerCommand(aliases = "help")
public class CommandHelp {

	static LiteralArgumentBuilder<TimerCommandInfo> COMMAND =
			literal("")
			.requires(info -> info.hasPermission("timer.help"))
			.then(argument("command", word()).executes(command -> {
				System.out.println("HELP - " + getString(command, "command"));
				return TimerCommandResponse.OK;
			}))
			.executes(command -> {
				System.out.println("HELP");
				return TimerCommandResponse.OK;
			});
}
