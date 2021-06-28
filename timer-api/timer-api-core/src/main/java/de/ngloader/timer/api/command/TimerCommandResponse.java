package de.ngloader.timer.api.command;

public interface TimerCommandResponse {

	public int OK = 0;
	public int ERROR = 1;
	public int HELP = 2;
	public int PERMISSION = 3;
	public int SYNTAX = 4;
}