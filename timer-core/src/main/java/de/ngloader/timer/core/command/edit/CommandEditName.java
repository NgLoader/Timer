package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.ArgumentBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.timer.Timer;

public class CommandEditName {

	public static final ArgumentBuilder<TimerCommandInfo, ?> COMMAND = literal("name")
			.then(argument("name", string())
					.executes(context -> {
						TimerCommandInfo commandInfo = context.getSource();
						Timer timer = CommandEdit.getTimer(context, true);
						if (timer == null) {
							return TimerCommandResponse.OK;
						}

						String name = getString(context, "name");
						return CommandEditName.handleEditName(commandInfo, timer, name);
					}));

	public static int handleEditName(TimerCommandInfo commandInfo, Timer timer, String name) {
		return TimerCommandResponse.OK;
	}
}