package de.ngloader.timer.api.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.i18n.TimerMessage;

public interface TimerCommand {

	public default String[] getAliases() {
		return new String[0];
	}

	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder();

	public default List<String> onTabComplete(Predicate<String> hasPermission, String[] args) {
		return Collections.emptyList();
	}

	public TimerMessage getDescriptionMessage();
	public TimerMessage getSyntaxMessage();
}