package de.ngloader.timer.core.command.edit;

import static de.ngloader.timer.api.command.TimerArgumentBuilder.argument;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.getString;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.literal;
import static de.ngloader.timer.api.command.TimerArgumentBuilder.string;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;
import de.ngloader.timer.core.TimerMessageOLD;
import de.ngloader.timer.core.util.SuggestionUtil;

public class CommandEdit implements TimerCommand {

	public static final Timer getTimer(CommandContext<TimerCommandInfo> context, boolean notify) {
		TimerCommandInfo commandInfo = context.getSource();
		TimerManager timerManager = commandInfo.getPlugin().getDefaultManager();

		String timerName = getString(context, "timerName");
		Timer timer = timerManager.getTimer(timerName);
		if (timer != null) {
			return timer;
		} else if (notify) {
			commandInfo.response(TimerMessageOLD.COMMAND_TIMER_NOT_FOUND, timerName);
		}

		return null;
	}

	@Override
	public String[] getAliases() {
		return new String[] { "e", "change", "modify" };
	}

	@Override
	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder() {
		return literal("edit")
				.requires(commandInfo -> commandInfo.hasAnyPermission(
						"timer.edit.message",
						"timer.edit.name",
						"timer.edit.sort",
						"timer.edit.status",
						"timer.edit.time.current",
						"timer.edit.time.max",
						"timer.edit.type"
						))
				.then(argument("timerName", string())
						.suggests(SuggestionUtil.SUGGEST_TIMER_NAME)
						.then(CommandEditMessage.COMMAND)
						.then(CommandEditName.COMMAND)
						.then(CommandEditSort.COMMAND)
						.then(CommandEditStatus.COMMAND)
						.then(CommandEditTime.COMMAND)
						.then(CommandEditType.COMMAND)
				).executes(context -> {
					return TimerCommandResponse.SYNTAX;
				});
	}

	@Override
	public TimerMessageOLD getDescriptionMessage() {
		return TimerMessageOLD.COMMAND_EDIT_DESCRIPTION;
	}

	@Override
	public TimerMessageOLD getSyntaxMessage() {
		return TimerMessageOLD.COMMAND_EDIT_SYNTAX;
	}

}
