package de.ngloader.timer.core.command.edit;

import de.ngloader.timer.api.command.TimerCommandInfo;
import de.ngloader.timer.api.command.TimerCommandResponse;

public class CommandEditName {

	public static int handleEditName(TimerCommandInfo commandInfo, String command, String name) {
		return TimerCommandResponse.OK;
	}
}