package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.timer.action.TimerActionType;
import de.ngloader.timer.api.timer.message.TimerMessageType;
import de.ngloader.timer.api.timer.sort.TimerSortType;
import de.ngloader.timer.api.timer.stop.TimerStopType;

public class CommandType implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "types" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("type")
				.requires(info -> info.hasPermission("timer.type"))
				.then(
						argument("type", string())
						.executes(command -> {
							TimerCommandInfo commandInfo = command.getSource();
							String inputType = getString(command, "type");
							return this.handleType(commandInfo, inputType);
						})
				).executes(command -> {
					TimerCommandInfo commandInfo = command.getSource();
					commandInfo.response(TimerMessage.COMMAND_TYPE_SYNTAX);
					return TimerCommandResponse.OK;
				});
	}

	public int handleType(TimerCommandInfo commandInfo, String inputType) {
		inputType = inputType.toLowerCase();

		commandInfo.response(TimerMessage.COMMAND_TYPE_START);
		switch (inputType) {
		case "action":
			for (TimerActionType type : TimerActionType.values()) {
				commandInfo.response(TimerMessage.COMMAND_TYPE_ENTRY, type.name());
			}
			break;

		case "message":
			for (TimerMessageType type : TimerMessageType.values()) {
				commandInfo.response(TimerMessage.COMMAND_TYPE_ENTRY, type.name());
			}
			break;

		case "sort":
			for (TimerSortType type : TimerSortType.values()) {
				commandInfo.response(TimerMessage.COMMAND_TYPE_ENTRY, type.name());
			}
			break;

		case "stop":
			for (TimerStopType type : TimerStopType.values()) {
				commandInfo.response(TimerMessage.COMMAND_TYPE_ENTRY, type.name());
			}
			break;

		default:
			commandInfo.response(TimerMessage.COMMAND_TYPE_SYNTAX);
		}
		commandInfo.response(TimerMessage.COMMAND_TYPE_END);

		return TimerCommandResponse.OK;
	}

	@Override
	public TimerMessage getDescriptionMessage() {
		return TimerMessage.COMMAND_TYPE_DESCRIPTION;
	}

	@Override
	public TimerMessage getSyntaxMessage() {
		return TimerMessage.COMMAND_TYPE_SYNTAX;
	}
}
