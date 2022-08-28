package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.word;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerMessage;

public class CommandHelp implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "help" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("")
				.requires(info -> info.hasPermission("timer.help"))
				.then(
						argument("command", word())
						.executes(command -> {
							TimerCommandInfo commandInfo = command.getSource();
							String input = getString(command, "command");
							return this.handleHelpCommand(commandInfo, input);
						})
				).executes(command -> {
					return TimerCommandResponse.SYNTAX;
				});
	}

	public int handleHelpCommand(TimerCommandInfo commandInfo, String command) {
		TimerCommandManager commandManager = commandInfo.getPlugin().getCommandManager();
		TimerCommand found = commandManager.getCommand(command);
		if (found == null) {
			commandInfo.response(TimerMessage.COMMAND_HELP_NOT_FOUND, command);
			return TimerCommandResponse.OK;
		}

		commandInfo.response(TimerMessage.COMMAND_HELP_START);
		commandInfo.response(TimerMessage.COMMAND_HELP_ENTRY_NAME, command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase());
		commandInfo.response(TimerMessage.COMMAND_HELP_ENTRY_DESCRIPTION, commandInfo.translate(found.getDescriptionMessage()));
		commandInfo.response(TimerMessage.COMMAND_HELP_ENTRY_ALIASES, String.join(", ", commandManager.getAliases(found)));
		commandInfo.response(TimerMessage.COMMAND_HELP_ENTRY_SYNTAX, commandInfo.translate(found.getSyntaxMessage()));
		commandInfo.response(TimerMessage.COMMAND_HELP_END);
		return TimerCommandResponse.OK;
	}

	@Override
	public TimerMessage getDescriptionMessage() {
		return TimerMessage.COMMAND_HELP_DESCRIPTION;
	}

	@Override
	public TimerMessage getSyntaxMessage() {
		return TimerMessage.COMMAND_HELP_SYNTAX;
	}
}
