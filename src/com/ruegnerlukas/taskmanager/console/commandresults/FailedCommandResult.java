package com.ruegnerlukas.taskmanager.console.commandresults;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;

import java.util.Collections;
import java.util.List;

public class FailedCommandResult extends CommandResult {


	public final List<String> errors;
	public final int index;



	public FailedCommandResult(String inputString, Command command, List<String> errors, int index) {
		super(inputString, command, false);
		this.errors = Collections.unmodifiableList(errors);
		this.index = index;
	}


}




