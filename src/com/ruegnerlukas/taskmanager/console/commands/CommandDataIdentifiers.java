package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.DataHandler;

public class CommandDataIdentifiers {


	public static Command create() {
		return new CommandBuilder()
				.text("data")
				.text("print")
				.text("identifiers")
				.optionalVariable("startswith", String.class)
				.setDescription("Prints all identifiers (that start with the given string)")
				.setExecutor(CommandDataIdentifiers::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		String startswith = result.getValueOrDefault("startswith", "");

		for (String identifier : DataHandler.getHandledIdentifiers()) {
			if (identifier.startsWith(startswith)) {
				ConsoleWindowHandler.print(identifier);
			}
		}

	}


}
