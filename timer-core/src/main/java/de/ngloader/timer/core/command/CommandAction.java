package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.greedyString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.api.timer.action.TimerActionType;
import de.ngloader.timer.core.util.StringUtil;

public class CommandAction implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "ac" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("action")
				.then(
						argument("name", greedyString())
						.then(
								argument("type", greedyString())
								.executes(command -> {
									TimerCommandInfo commandInfo = command.getSource();
									String name = getString(command, "name");
									String type = getString(command, "type");
									return this.handleActionNameType(commandInfo, name, type);
								})
						)
				);
	}

	public int handleActionNameType(TimerCommandInfo commandInfo, String name, String type) {
		if (!commandInfo.isDatabaseConnected(true)) {
			return TimerCommandResponse.OK;
		}

		TimerPlugin plugin = commandInfo.getPlugin();
		TimerManager manager = plugin.getDefaultManager();

		Timer timer = manager.getTimer(name);

		if (timer != null ?
				!commandInfo.hasOnePermission(
					"timer.action.*",
					"timer.action." + timer.getName().toLowerCase()) :
				!commandInfo.hasPermission("timer.action.*")) {
			return TimerCommandResponse.PERMISSION;
		}

		if (manager.getTimer(name) == null) {
			commandInfo.response(TimerMessage.COMMAND_TIMER_NOT_FOUND, name);
			return TimerCommandResponse.OK;
		}

		TimerActionType actionType = TimerActionType.search(type);
		if (actionType == null) {
			commandInfo.response(TimerMessage.COMMAND_ACTION_ACTION_NOT_FOUND, type);
			return TimerCommandResponse.OK;
		}

		timer.setActionType(actionType);
		commandInfo.response(TimerMessage.COMMAND_ACTION_CHANGED, timer.getName(), StringUtil.toFirstUpper(type));
		return TimerCommandResponse.OK;
	}

	@Override
	public TimerMessage getDescriptionMessage() {
		return TimerMessage.COMMAND_ACTION_DESCRIPTION;
	}

	@Override
	public TimerMessage getSyntaxMessage() {
		return TimerMessage.COMMAND_ACTION_SYNTAX;
	}

}
