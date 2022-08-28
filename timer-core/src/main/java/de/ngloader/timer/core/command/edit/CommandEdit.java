package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.greedyString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerMessage;

public class CommandEdit implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "e", "change", "modify" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("edit")
				.then(argument("command", greedyString())
						.then(literal("name")
								.then(argument("name", greedyString())
										.executes(context -> {
											TimerCommandInfo commandInfo = context.getSource();
											String command = getString(context, "command");
											String name = getString(context, "name");
											return CommandEditName.handleEditName(commandInfo, command, name);
										})))
						.then(literal("time")
								.then(literal("current")
										.then(argument("time", greedyString())
												.executes(context -> {
													return 0;
												})
												.suggests((command, builder) -> {
													return CommandEditTime.suggest(command, builder);
												})))
								.then(literal("max")))
						
				.executes(context -> {
					return TimerCommandResponse.SYNTAX;
				}));
	}

	@Override
	public TimerMessage getDescriptionMessage() {
		return TimerMessage.COMMAND_EDIT_DESCRIPTION;
	}

	@Override
	public TimerMessage getSyntaxMessage() {
		return TimerMessage.COMMAND_EDIT_SYNTAX;
	}

}
