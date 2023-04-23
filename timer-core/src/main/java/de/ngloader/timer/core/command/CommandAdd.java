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
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.core.TimerMessageOLD;
import de.ngloader.timer.core.timer.ImplTimer;

public class CommandAdd implements TimerCommand {

	@Override
	public String[] getAliases() {
		return new String[] { "create" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("add")
				.requires(info -> info.hasPermission("timer.add"))
				.then(
						argument("name", greedyString())
						.executes(command -> {
							TimerCommandInfo commandInfo = command.getSource();
							String name = getString(command, "name");
							return this.handleAddName(commandInfo, name);
						})
				).executes(command -> {
					return TimerCommandResponse.SYNTAX;
				});
	}

	public int handleAddName(TimerCommandInfo commandInfo, String name) {
		if (!commandInfo.isDatabaseConnected(true)) {
			return TimerCommandResponse.OK;
		}

		TimerPlugin plugin = commandInfo.getPlugin();
		TimerManager manager = plugin.getDefaultManager();

		if (manager.getTimer(name) != null) {
			commandInfo.response(TimerMessageOLD.COMMAND_ADD_NAME_ALREADY_TAKEN, name);
			return TimerCommandResponse.OK;
		}

		Timer timer = new ImplTimer(plugin, manager, plugin.generateUUID(), name, false);
		plugin.getDatabaseManager().getDatabase().createTimer(timer);
		manager.addTimer(timer);

		commandInfo.response(TimerMessageOLD.COMMAND_ADD_ADDED, name);
		return TimerCommandResponse.OK;
	}

	@Override
	public TimerMessageOLD getDescriptionMessage() {
		return TimerMessageOLD.COMMAND_ADD_DESCRIPTION;
	}

	@Override
	public TimerMessageOLD getSyntaxMessage() {
		return TimerMessageOLD.COMMAND_ADD_SYNTAX;
	}
}
