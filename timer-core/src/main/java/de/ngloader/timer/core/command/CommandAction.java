package de.ngloader.timer.core.command;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.api.timer.action.TimerActionType;
import de.ngloader.timer.core.TimerMessageOLD;
import de.ngloader.timer.core.util.StringUtil;
import de.ngloader.timer.core.util.SuggestionUtil;

public class CommandAction implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "ac" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("action")
				.requires(commandInfo -> commandInfo.hasPermission("timer.action"))
				.then(
						argument("name", string())
						.suggests(SuggestionUtil.SUGGEST_TIMER_NAME)
						.then(
								argument("type", string())
								.suggests(SuggestionUtil.suggest(TimerActionType.class))
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
				!commandInfo.hasAnyPermission(
					"timer.action.*",
					"timer.action." + timer.getName().toLowerCase()) :
				!commandInfo.hasPermission("timer.action.*")) {
			return TimerCommandResponse.PERMISSION;
		}

		if (manager.getTimer(name) == null) {
			commandInfo.response(TimerMessageOLD.COMMAND_TIMER_NOT_FOUND, name);
			return TimerCommandResponse.OK;
		}

		TimerActionType actionType = TimerActionType.search(type);
		if (actionType == null) {
			commandInfo.response(TimerMessageOLD.COMMAND_ACTION_ACTION_NOT_FOUND, type);
			return TimerCommandResponse.OK;
		}

		timer.setActionType(actionType);
		commandInfo.response(TimerMessageOLD.COMMAND_ACTION_CHANGED, timer.getName(), StringUtil.toFirstUpper(type));
		return TimerCommandResponse.OK;
	}

	@Override
	public TimerMessageOLD getDescriptionMessage() {
		return TimerMessageOLD.COMMAND_ACTION_DESCRIPTION;
	}

	@Override
	public TimerMessageOLD getSyntaxMessage() {
		return TimerMessageOLD.COMMAND_ACTION_SYNTAX;
	}

}
