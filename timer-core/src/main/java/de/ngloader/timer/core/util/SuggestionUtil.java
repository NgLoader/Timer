package de.ngloader.timer.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;

import de.ngloader.timer.api.TimeMapper;
import de.ngloader.timer.api.TimerPlugin;
import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandManager;
import de.ngloader.timer.api.timer.Timer;

public class SuggestionUtil {

	public static final SuggestionProvider<TimerCommandInfo> SUGGEST_TIMER_NAME = (context, builder) -> {
		TimerPlugin plugin = context.getSource().getPlugin();
		Set<Timer> timers = plugin.getDefaultManager().getTimers();

		return compareSuggest(builder, timers.stream().map(timer -> timer.getName()));
	};

	public static final SuggestionProvider<TimerCommandInfo> SUGGEST_COMMAND_ROOT = (context, builder) -> {
		TimerPlugin plugin = context.getSource().getPlugin();
		TimerCommandManager commandManager = plugin.getCommandManager();
		CommandDispatcher<TimerCommandInfo> dispatcher = commandManager.getCommandDispatcher();
		Collection<CommandNode<TimerCommandInfo>> children = dispatcher.getRoot().getChildren();
		
		return compareSuggest(builder, children.stream().map(child -> child.getName()));
	};

	public static final SuggestionProvider<TimerCommandInfo> SUGGEST_TIME_INPUT = (context, builder) -> {
		String input = builder.getRemaining();
		String lastLetter = input.isBlank() ? "" : input.substring(input.length() - 1);

		if (RegexUtil.INTEGER.matches(lastLetter)) {
			return compareSuggest(builder, "", input, StringUtil.enumNames(TimeMapper.class));
		}

		if (!lastLetter.isBlank()) {
			StringBuilder lastWordBuilder = new StringBuilder();
			for (int i = input.length() - 1; i >= 0; i--) {
				String character = String.valueOf(input.charAt(i));
				if (RegexUtil.INTEGER.matches(character)) {
					break;
				}
				lastWordBuilder.append(character);
			}

			String lastWord = lastWordBuilder.reverse().toString();
			String inputWithoutLastWord = input.substring(0, input.length() - lastWord.length());

			List<TimeMapper> suggestions = TimeMapper.suggest(lastWord);
			if (suggestions.size() > 1) {
				return compareSuggest(builder, lastWord, inputWithoutLastWord, suggestions.stream().map(suggestion -> suggestion.name().toLowerCase()));
			} else if (!suggestions.isEmpty()) {
				String suggestionName = suggestions.get(0).name().toLowerCase();

				if (lastWord != suggestionName) {
					builder.suggest(inputWithoutLastWord + suggestionName);
				}
			} else {
				builder.suggest(inputWithoutLastWord, new LiteralMessage(String.format("§cInvalid input at §8\"§c%s§4%s§8\"", inputWithoutLastWord, lastWord)));
				return builder.buildFuture();
			}
		}

		for (int i = 0; i < 9; i++) {
			builder.suggest(input + i);
		}

		return builder.buildFuture();
	};

	public static final Long getSuggestedTime(String input) {
		long ticks = 0;

		String time = "";
		String type = "";

		for (int i = 0; i < input.length(); i++) {
			String character = String.valueOf(input.charAt(i));

			if (RegexUtil.INTEGER.matches(character)) {
				if (type.length() == 0) {
					time += character;
					continue;
				}
			} else if (time.isBlank() && type.isBlank()) {
				return null; // invalid
			} else {
				type += character;
				continue;
			}

			TimeMapper resultType = TimeMapper.suggestFirst(type);
			if (resultType == null) {
				return null; // invalid
			}

			ticks += resultType.getTicks() * Integer.valueOf(time);

			time = character;
			type = "";
		}

		if (time.length() != 0) {
			if (type.length() != 0) {
				TimeMapper resultType = TimeMapper.suggestFirst(type);
				if (resultType == null) {
					return null; // invalid
				}

				ticks += resultType.getTicks() * Integer.valueOf(time);
			} else {
				ticks += TimeMapper.SECOND.getTicks() * Integer.valueOf(time);
			}
		}

		return ticks;
	}

	public static SuggestionProvider<TimerCommandInfo> suggest(Class<? extends Enum<?>> enumClass) {
		return (context, builder) -> compareSuggest(builder, Stream.of(enumClass.getEnumConstants()).map(value -> value.name()));
	}

	public static SuggestionProvider<TimerCommandInfo> suggest(String... values) {
		return (context, builder) -> compareSuggest(builder, Stream.of(values));
	}

	private static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, Stream<String> values) {
		return compareSuggest(builder, values.toArray(String[]::new));
	}

	private static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String... values) {
		String input = builder.getRemaining().toLowerCase();
		for (String value : values) {
			String name = value.toLowerCase();
			if (name.startsWith(input) || name.contains(input)) {
				builder.suggest(value.toLowerCase());
			}
		}
		return builder.buildFuture();
	}

	private static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String input, String prefix, Stream<String> values) {
		return compareSuggest(builder, input, prefix, values.toArray(String[]::new));
	}


	private static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String input, String prefix, String... values) {
		input = input.toLowerCase();

		for (String value : values) {
			String name = value.toLowerCase();
			if (name.startsWith(input) || name.contains(input)) {
				builder.suggest(prefix + value.toLowerCase());
			}
		}
		return builder.buildFuture();
	}
}
