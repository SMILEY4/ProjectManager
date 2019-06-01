package com.ruegnerlukas.taskmanager.console.commandresults;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;

public class CommandResult {


	public final String inputString;
	public final Command command;
	public final boolean failed;


	public CommandResult(String inputString, Command command, boolean failed) {
		this.inputString = inputString;
		this.command = command;
		this.failed = failed;
	}




}
