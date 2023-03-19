package de.ngloader.timer.api.command;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;

import de.ngloader.timer.api.TimerPlugin;

public interface TimerCommandManager {

	public void registerCommand(TimerCommand command);

	public boolean executeCommand(String[] args, TimerCommandInfo commandInfo);
	public boolean executeCommand(String[] args, Predicate<String> permission, Consumer<String> response);

	public Suggestions onTabComplete(String[] args, TimerCommandInfo commandInfo);
	public Suggestions onTabComplete(String[] args, Predicate<String> permission);

	public Set<String> getAliases(TimerCommand command);
	public TimerCommand getCommand(String command);

	public CommandDispatcher<TimerCommandInfo> getCommandDispatcher();

	public TimerPlugin getPlugin();
}