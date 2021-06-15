package de.ngloader.timer.api.command;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.ngloader.timer.api.TimerPlugin;

public interface TimerCommandManager {

	public void registerCommand(Class<?> clazz, LiteralArgumentBuilder<TimerCommandInfo> command);
	public void registerCommand(TimerCommand annotation, LiteralArgumentBuilder<TimerCommandInfo> command);

	public boolean executeCommand(String[] args, TimerCommandInfo commandInfo);
	public boolean executeCommand(String[] args, Predicate<String> permission, Consumer<String> response);

	public CommandDispatcher<TimerCommandInfo> getCommandDispatcher();

	public TimerPlugin getPlugin();
}