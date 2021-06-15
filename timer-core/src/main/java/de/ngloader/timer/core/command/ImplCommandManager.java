package de.ngloader.timer.core.command;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerArgumentBuilder;
import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.i18n.TimerModule;

public class ImplCommandManager implements TimerCommandManager {

	private final TimerPlugin plugin;

	private final CommandDispatcher<TimerCommandInfo> commandDispatcher = new CommandDispatcher<>();

	public ImplCommandManager(TimerPlugin plugin) {
		this.plugin = plugin;

		this.registerCommand(CommandAdd.class, CommandAdd.COMMAND);
		this.registerCommand(CommandCmd.class, CommandCmd.COMMAND);
		this.registerCommand(CommandEdit.class, CommandEdit.COMMAND);
		this.registerCommand(CommandInfo.class, CommandInfo.COMMAND);
		this.registerCommand(CommandList.class, CommandList.COMMAND);
		this.registerCommand(CommandLoad.class, CommandLoad.COMMAND);
		this.registerCommand(CommandReload.class, CommandReload.COMMAND);
		this.registerCommand(CommandSave.class, CommandSave.COMMAND);
		this.registerCommand(CommandType.class, CommandType.COMMAND);
		this.registerCommand(CommandHelp.class, CommandHelp.COMMAND);
	}

	@Override
	public void registerCommand(Class<?> clazz, LiteralArgumentBuilder<TimerCommandInfo> command) {
		Objects.requireNonNull(command, "Command is null");

		this.registerCommand(clazz.getAnnotation(TimerCommand.class), command);
	}

	@Override
	public void registerCommand(TimerCommand annotation, LiteralArgumentBuilder<TimerCommandInfo> command) {
		Objects.requireNonNull(command, "Command is null");

		CommandNode<TimerCommandInfo> node = this.commandDispatcher.register(command);

		if (annotation != null) {
			for (String alias : annotation.aliases()) {
				if (alias.isEmpty()) {
					continue;
				}

				LiteralArgumentBuilder<TimerCommandInfo> aliasBuilder = TimerArgumentBuilder.literal(alias)
						.executes(node.getCommand())
						.redirect(node);
				this.commandDispatcher.register(aliasBuilder);
			}
		}
	}

	@Override
	public boolean executeCommand(String[] args, Predicate<String> permission, Consumer<String> response) {
		return this.executeCommand(args, new TimerCommandInfo() {
			
			@Override
			public BiConsumer<TimerMessage, Object[]> response() {
				TimerLanguageService languageService = ImplCommandManager.this.plugin.getLanguageService();
				return (message, args) -> response.accept(languageService.getPrefix() + languageService.translate(message, args));
			}
			
			@Override
			public Predicate<String> hasPermission() {
				return permission;
			}
		});
	}

	@Override
	public boolean executeCommand(String[] args, TimerCommandInfo commandInfo) {
		try {
			int code = this.commandDispatcher.execute(String.join(" ", args), commandInfo);

			switch (code) {
			case TimerCommandResponse.OK:
				return true;

			case TimerCommandResponse.HELP:
				return this.executeCommand(args.length > 0 ? new String[] { "help", args[0] } : new String[] { "help" }, commandInfo);

			case TimerCommandResponse.ERROR:
				this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §c" + String.join(" ", args) + "§8! §cPlease §4report §cthis to the projekt maintainer§8.!");
				return false;

			default:
				this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §c" + String.join(" ", args) + " §7returned unknown code§8! §cPlease §4report §cthis to the projekt maintainer§8.!");
				return false;
			}
		} catch (CommandSyntaxException e) {
			commandInfo.response(TimerMessage.COMMAND_UNKOWN_SYNTAX);
			return false;
		}
	}

	@Override
	public CommandDispatcher<TimerCommandInfo> getCommandDispatcher() {
		return this.commandDispatcher;
	}

	@Override
	public TimerPlugin getPlugin() {
		return this.plugin;
	}
}