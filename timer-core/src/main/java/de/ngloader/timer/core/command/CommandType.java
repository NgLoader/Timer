package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.timer.action.TimerActionType;
import de.ngloader.timer.api.timer.message.TimerMessageType;
import de.ngloader.timer.api.timer.sort.TimerSortType;
import de.ngloader.timer.api.timer.stop.TimerStopType;
import de.ngloader.timer.core.util.StringUtil;

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
						literal("action")
						.executes(command -> {
							return this.handleType(command.getSource(), StringUtil.enumNamesAsList(TimerActionType.class));
						}))
				.then(
						literal("message")
						.executes(command -> {
							return this.handleType(command.getSource(), StringUtil.enumNamesAsList(TimerMessageType.class));
						}))
				.then(
						literal("sort")
						.executes(command -> {
							return this.handleType(command.getSource(), StringUtil.enumNamesAsList(TimerSortType.class));
						}))
				.then(
						literal("stop")
						.executes(command -> {
							return this.handleType(command.getSource(), StringUtil.enumNamesAsList(TimerStopType.class));
						})
				).executes(command -> {
					TimerCommandInfo commandInfo = command.getSource();
					commandInfo.response(TimerMessage.COMMAND_TYPE_SYNTAX);
					return TimerCommandResponse.OK;
				});
	}

	public int handleType(TimerCommandInfo commandInfo, List<String> names) {
		commandInfo.response(TimerMessage.COMMAND_TYPE_START);
		names.forEach(name -> commandInfo.response(TimerMessage.COMMAND_TYPE_ENTRY, name));
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
