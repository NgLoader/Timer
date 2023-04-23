package de.ngloader.timer.core.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerArgumentBuilder;
import de.ngloader.timer.api.command.TimerCommand;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.command.TimerCommandResponse;
import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.core.TimerMessageOLD;
import de.ngloader.timer.core.command.edit.CommandEdit;

public class ImplCommandManager implements TimerCommandManager {

	private static Suggestions suggestionEmpty = new SuggestionsBuilder("", 0).build();

	private final TimerPlugin plugin;

	private final CommandDispatcher<TimerCommandInfo> commandDispatcher = new CommandDispatcher<>();

	private final Map<String, TimerCommand> commandByAlias = new HashMap<>();
	private final Map<TimerCommand, Set<String>> aliasByCommand = new HashMap<>();

	public ImplCommandManager(TimerPlugin plugin) {
		this.plugin = plugin;

		this.registerCommand(new CommandAction());
		this.registerCommand(new CommandAdd());
		this.registerCommand(new CommandType());
		this.registerCommand(new CommandHelp());
		this.registerCommand(new CommandEdit());
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
	public boolean executeCommand(String[] args, TimerCommandInfo commandInfo) {
		try {
			int code = this.commandDispatcher.execute(String.join(" ", args), commandInfo);

			switch (code) {
			case TimerCommandResponse.OK:
				return true;

			case TimerCommandResponse.HELP:
				return this.executeCommand(args.length > 0 ? new String[] { "help", args[0] } : new String[] { "help" }, commandInfo);

			case TimerCommandResponse.PERMISSION:
//				TimerCommand command = this.getCommand(args);
//				commandInfo.response(TimerMessage.COMMAND_NO_PERMISSION, StringUtil.toFirstUpper(command != null ? command.getCommandBuilder().getLiteral() : ""));
				commandInfo.response(TimerMessageOLD.COMMAND_NO_PERMISSION, String.join(", ", commandInfo.failedPermissionCheck()));
				return true;

			case TimerCommandResponse.SYNTAX:
				return this.sendSyntaxMessage(args, commandInfo);

			case TimerCommandResponse.ERROR:
				this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §8\"§c" + String.join(" ", args) + "§8\"§8! §cPlease §4report §cthis to the projekt maintainer§8.!");

//				TimerCommand command = this.getCommand(args);
				commandInfo.response(TimerMessageOLD.COMMAND_ERROR_OCCURED, String.join(" ", args));
				return false;
			}
		} catch (CommandSyntaxException e) {
			return this.sendSyntaxMessage(args, commandInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.plugin.logError(TimerModule.MODULE_COMMAND, "Error by executing command §8\"§c" + String.join(" ", args) + "§8\" §7returned unknown code§8! §cPlease §4report §cthis to the projekt maintainer§8.!");
		return false;
	}

	@Override
	public boolean executeCommand(String[] args, Predicate<String> permission, Consumer<String> response) {
		return this.executeCommand(args, new ImplTimerCommandInfo(this.plugin, permission, response));
//		return this.executeCommand(args, new TimerCommandInfo() {
//
//			@Override
//			public TimerPlugin getPlugin() {
//				return ImplCommandManager.this.plugin;
//			}
//			
//			@Override
//			public BiConsumer<TimerMessage, Object[]> response() {
//				TimerLanguageService languageService = ImplCommandManager.this.languageService;
//				return (message, args) -> response.accept(languageService.getPrefix() + languageService.translate(message, args));
//			}
//			
//			@Override
//			public Predicate<String> hasPermission() {
//				return permission;
//			}
//		});
	}

	@Override
	public Suggestions onTabComplete(String[] args, TimerCommandInfo commandInfo) {
		StringReader cursor = new StringReader(String.join(" ", args));
		if (cursor.canRead() && cursor.peek() == '/') {
			cursor.skip();
		}

		try {
			ParseResults<TimerCommandInfo> parseResults = this.commandDispatcher.parse(cursor, commandInfo);
			Suggestions suggestions = this.commandDispatcher.getCompletionSuggestions(parseResults).get();
			if (suggestions == null || suggestions.isEmpty()) {
				return suggestionEmpty;
			}

			return suggestions;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			return suggestionEmpty;
		}

		return suggestionEmpty;
	}

	@Override
	public Suggestions onTabComplete(String[] args, Predicate<String> permission) {
		return this.onTabComplete(args, new ImplTimerCommandInfo(this.plugin, permission, null));
//		return this.onTabComplete(args, new TimerCommandInfo() {
//
//			@Override
//			public TimerPlugin getPlugin() {
//				return ImplCommandManager.this.plugin;
//			}
//			
//			@Override
//			public BiConsumer<TimerMessage, Object[]> response() {
//				return null;
//			}
//			
//			@Override
//			public Predicate<String> hasPermission() {
//				return permission;
//			}
//		});
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
		commandInfo.response(TimerMessageOLD.COMMAND_UNKOWN_SYNTAX);
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