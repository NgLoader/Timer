package de.ngloader.timer.core.command.edit;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.core.RegexUtil;

public class CommandEditTime {

	public static int handle(TimerCommandInfo commandInfo, String command) {
		return TimerCommandResponse.OK;
	}

	public static CompletableFuture<Suggestions> suggest(CommandContext<TimerCommandInfo> command,
			SuggestionsBuilder builder) {
		String input = builder.getInput();
		String lastLetter = input.isBlank() ? "" : input.substring(input.length() - 1);

		if (RegexUtil.INTEGER.matches(lastLetter)) {
			builder
				.suggest("Year")
				.suggest("Month")
				.suggest("Week")
				.suggest("Hour")
				.suggest("Minute")
				.suggest("Second")
				.suggest("Tick");
		} else {
			for (int i = 0; i < 9; i++) {
				builder.suggest(i);
			}
		}

		return builder.buildFuture();
	}
}
