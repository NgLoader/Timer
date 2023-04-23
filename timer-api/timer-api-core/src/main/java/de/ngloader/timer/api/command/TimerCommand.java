package de.ngloader.timer.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public interface TimerCommand {

	/**
	 * A list of all alises excluding the main command!
	 * 
	 * @return all aliases excluding the main command!
	 */
	public default String[] getAliases() {
		return new String[0];
	}

	public LiteralArgumentBuilder<TimerCommandInfo> getCommandBuilder();

	public String getDescriptionMessage();
	public String getSyntaxMessage();
}