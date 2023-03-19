package de.ngloader.timer.api.i18n;

import java.util.HashMap;
import java.util.Map;

public enum TimerMessage {

	CORE_LOADING("§7Loading§8."),
	CORE_LOADING_TRANSLATION("§7Loading translations§8."),
	CORE_LOADED_TRANSLATION("§7Loaded §e{0} §7translations§8."),
	CORE_LOADED("§7Loaded§8."),
	CORE_DISABLING("§7Disabling§8."),
	CORE_DISABLED("§7Disabled§8."),

	DATABASE_STORING_TIMERS_TO_DATABASE("§7Storing timers to database§8."),
	DATABASE_CLOSING_DATABASE_CONNECTION("§7Closing database connection§8."),
	DATABASE_CLOSED_DATABASE_CONNECTION("§7Closed database connection§8."),
	DATABASE_REMOVED_ALL_TIMERS("§7Removed §call §7timer§8."),
	DATABASE_OPENING_DATABASE_CONNECTION("§7Opening database connection§8."),
	DATABASE_OPENED_DATABASE_CONNECTION("§7Opened database connection§8."),
	DATABASE_LOADING_TIMER("§7Loading timer§8."),
	DATABASE_LOADED_TIMER("§7Loaded §e{0} §7timer§8."),
	DATABASE_CONVERTING_TIMERS_FROM_TO("§7Converting timers from §e{0} §7to §e{1}§8."),
	DATABASE_FOUND_TIMER("§7Found §e{0} §7timer§8."),
	DATABASE_COPIED_TIMER("§7Copied §e{0} §7timer§8."),
	DATABASE_CONVERTING_FINISHED("§7Converting finished§8."),

	COMMAND_TIMER_NOT_FOUND("Timer {0} not found"),

	COMMAND_DATABASE_IS_NOT_CONNECTED("§7Currently no database connection is §copen§8!"),

	COMMAND_TYPE_START("§8[]§7=====§8<>§7=====§6< §eTypes §6>§7=====§8<>§7=====§8[]"),
	COMMAND_TYPE_ENTRY("  §8- §e{0}"),
	COMMAND_TYPE_END("§8[]§7=====§8<>§7=====§6< §eTypes §6>§7=====§8<>§7=====§8[]"),
	COMMAND_TYPE_DESCRIPTION("§7List of the all types"),
	COMMAND_TYPE_SYNTAX("§8/§atimer §etypes §8<§eAction§7|§eMessage§7|§eSort§7|§eStop§8>"),

	COMMAND_ADD_NAME_ALREADY_TAKEN("§7Name §8'§e{0}§8' §7already exists§8."),
	COMMAND_ADD_ADDED("§7Der Timer §8'§e{0}§8' §7wurde erfolgreich hinzugefügt§8."),
	COMMAND_ADD_DESCRIPTION("§7Adds a new timer"),
	COMMAND_ADD_SYNTAX("§8/§atimer §eadd §8<§eName§8>"),

	COMMAND_HELP_START("§8[]§7=====§8<>§7=====§6< §eHelp §6>§7=====§8<>§7=====§8[]"),
	COMMAND_HELP_ENTRY_NAME("  §7Name§8: §e{0}"),
	COMMAND_HELP_ENTRY_DESCRIPTION("  §7Description§8: §e{0}"),
	COMMAND_HELP_ENTRY_ALIASES("  §7Aliases§8: §e{0}"),
	COMMAND_HELP_ENTRY_SYNTAX("  §7Syntax§8: §e{0}"),
	COMMAND_HELP_END("§8[]§7=====§8<>§7=====§6< §eHelp §6>§7=====§8<>§7=====§8[]"),
	COMMAND_HELP_NOT_FOUND("$7The command §8'§e{0}§8' §7dosen't exist§8."),
	COMMAND_HELP_DESCRIPTION("§7Find more informations about a command"),
	COMMAND_HELP_SYNTAX("§8/§atimer §ehelp §8[§ecommand§8]"),

	COMMAND_ACTION_CHANGED("Timer {0} action changed to {1}"),
	COMMAND_ACTION_ACTION_NOT_FOUND("Action {0} not found"),
	COMMAND_ACTION_DESCRIPTION("description"),
	COMMAND_ACTION_SYNTAX("syntax"),

	COMMAND_EDIT_DESCRIPTION("description"),
	COMMAND_EDIT_SYNTAX("syntax"),

	COMMAND_NO_PERMISSION("§7You don't have the following permission §8\"§c{0}§8\""),
	COMMAND_ERROR_OCCURED("§cA error occured by executing the following command §8\"{0}§8\""),
	COMMAND_UNKOWN_SYNTAX("§7Unknown syntax§8.");

	public static Map<TimerMessage, String> toMap() {
		Map<TimerMessage, String> messages = new HashMap<>();
		for (TimerMessage message : values()) {
			messages.put(message, message.getMessage());
		}
		return messages;
	}

	private final String message;

	private TimerMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}