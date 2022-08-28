package de.ngloader.timer.core.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerArgumentBuilder;
import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerLanguageService;
import de.ngloader.timer.api.i18n.TimerMessage;
import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.core.StringUtil;

public class ImplCommandManager implements TimerCommandManager {

	private final TimerPlugin plugin;
	private final TimerLanguageService languageService;

	private final CommandDispatcher<TimerCommandInfo> commandDispatcher = new CommandDispatcher<>();

	private final Map<String, TimerCommand> commandByAlias = new HashMap<>();
	private final Map<TimerCommand, Set<String>> aliasByCommand = new HashMap<>();

	public ImplCommandManager(TimerPlugin plugin) {
		this.plugin = plugin;
		this.languageService = this.plugin.getLanguageService();

		this.registerCommand(new CommandAction());
		this.registerCommand(new CommandAdd());
		this.registerCommand(new CommandType());
		this.registerCommand(new CommandHelp());
	}

	@Override
	public void registerCommand(TimerCommand command) {
		Objects.requireNonNull(command, "Command is null");

		LiteralCommandNode<TimerCommandInfo> node = this.commandDispatcher.register(command.getCommandBuilder());
		this.addCommand(command, node.getLiteral().toLowerCase());

		for (String alias : command.getAliases()) {
			this.addCommand(command, alias);

			LiteralArgumentBuilder<TimerCommandInfo> aliasBuilder = TimerArgumentBuilder.literal(alias)
					.executes(node.getCommand())
					.redirect(node);
			this.commandDispatcher.register(aliasBuilder);
		}
	}

	private void addCommand(TimerCommand command, String alias) {
		this.commandByAlias.put(alias, command);

		if (alias == null || alias.isEmpty()) {
			return;
		}

		Set<String> aliases = this.aliasByCommand.get(command);
		if (aliases == null) {
			aliases = new HashSet<>();
			this.aliasByCommand.put(command, aliases);
		}
		aliases.add(alias);
	}

	@Override
	public boolean executeCommand(String[] args, Predicate<String> permission, Consumer<String> response) {
		return this.executeCommand(args, new TimerCommandInfo() {

			@Override
			public TimerPlugin getPlugin() {
				return ImplCommandManager.this.plugin;
			}
			
			@Override
			public BiConsumer<TimerMessage, Object[]> response() {
				TimerLanguageService languageService = ImplCommandManager.this.languageService;
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

			case TimerCommandResponse.PERMISSION:
				TimerCommand command = this.getCommand(args);
				commandInfo.response(TimerMessage.COMMAND_NO_PERMISSION, StringUtil.toFirstUpper(command != null ? command.getCommandBuilder().getLiteral() : ""));
				return true;

			case TimerCommandResponse.SYNTAX:
				return this.sendSyntaxMessage(args, commandInfo);

			case TimerCommandResponse.ERROR:
				this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §c" + String.join(" ", args) + "§8! §cPlease §4report §cthis to the projekt maintainer§8.!");
				return false;
			}
		} catch (CommandSyntaxException e) {
			return this.sendSyntaxMessage(args, commandInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §c" + String.join(" ", args) + " §7returned unknown code§8! §cPlease §4report §cthis to the projekt maintainer§8.!");
		return false;
	}

	private TimerCommand getCommand(String[] args) {
		return this.commandByAlias.get(args.length > 0 ? args[0] : "");
	}

	private boolean sendSyntaxMessage(String[] args, TimerCommandInfo commandInfo) {
		TimerCommand command = this.getCommand(args);
		if (command != null && command.getSyntaxMessage() != null) {
			commandInfo.response(command.getSyntaxMessage());
			return true;
		}
		commandInfo.response(TimerMessage.COMMAND_UNKOWN_SYNTAX);
		return false;
	}

	@Override
	public Set<String> getAliases(TimerCommand command) {
		return this.aliasByCommand.get(command);
	}

	@Override
	public TimerCommand getCommand(String command) {
		return this.commandByAlias.get(command.toLowerCase());
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