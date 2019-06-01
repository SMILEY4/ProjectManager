package com.ruegnerlukas.taskmanager.console.commandresults;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;

import java.util.Collections;
import java.util.Map;

public class SuccessfulCommandResult extends CommandResult {


	public final Map<String, Object> parameter;




	public SuccessfulCommandResult(String inputString, Command command, Map<String, Object> parameter) {
		super(inputString, command, false);
		this.parameter = Collections.unmodifiableMap(parameter);
	}




	public boolean hasParameter(String name) {
		return parameter.containsKey(name);
	}




	public <T> T getValue(String name) {
		return (T) parameter.get(name);
	}


	public <T> T getValueOrDefault(String name, T defaultValue) {
		return hasParameter(name) ? getValue(name) : defaultValue;
	}


}
