package de.ngloader.timer.bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class BrigadierAdapter implements Listener {

	private static final Class<?> CRAFT_SERVER_CLASS = ReflectionUtil.getCraftBukkitClass("CraftServer");

	private static final Constructor<?> COMMAND_WRAPPER_CONSTRUCTOR = ReflectionUtil.getConstructor(
			ReflectionUtil.getCraftBukkitClass("command.BukkitCommandWrapper"), CRAFT_SERVER_CLASS, Command.class);

	private static final Method COMMAND_DISPATCHER_METHOD = ReflectionUtil
			.getMethod(ReflectionUtil.getMinecraftServer("MinecraftServer"), "getCommandDispatcher");
	private static final Method BRIGADIER_DISPATCHER_METHOD = Arrays
			.stream(ReflectionUtil.getMinecraftServerClass("CommandDispatcher").getDeclaredMethods())
			.filter(method -> method.getParameterCount() == 0)
			.filter(method -> CommandDispatcher.class.isAssignableFrom(method.getReturnType()))
			.peek(method -> method.setAccessible(true)).findFirst().get();

	private static final Field CUSTOM_SUGGESTIONS_FIELD = ReflectionUtil.getField(ArgumentCommandNode.class, "customSuggestions");
	private static final Field CONSOLE_FIELD = ReflectionUtil.getField(CRAFT_SERVER_CLASS, "console");

	public void register(Command command, LiteralCommandNode<?> node) {
		try {
			Object wrapper = COMMAND_WRAPPER_CONSTRUCTOR.newInstance(Bukkit.getServer(), command);
			this.setCustomSuggestionProvider(node, (SuggestionProvider<?>) wrapper);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void setCustomSuggestionProvider(CommandNode<?> node, SuggestionProvider<?> suggestionProvider) {
		if (node instanceof ArgumentCommandNode) {
			ArgumentCommandNode<?, ?> argumentCommandNode = (ArgumentCommandNode<?, ?>) node;
			try {
				CUSTOM_SUGGESTIONS_FIELD.set(argumentCommandNode, suggestionProvider);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for (CommandNode<?> child : node.getChildren()) {
			this.setCustomSuggestionProvider(child, suggestionProvider);
		}
	}

	public CommandDispatcher<?> getDispatcher() {
		try {
			Object minecraftServer = CONSOLE_FIELD.get(Bukkit.getServer());
			Object commandDispatcher = COMMAND_DISPATCHER_METHOD.invoke(minecraftServer);
			return (CommandDispatcher<?>) BRIGADIER_DISPATCHER_METHOD.invoke(commandDispatcher);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}

	}
}