package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import javafx.scene.paint.Color;

public class CommandProjectSetIDCounter {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("set-id-counter")
				.variable("value", Integer.class)
				.setDescription("Sets the id-counter of the opened project to the given value.")
				.setExecutor(CommandProjectSetIDCounter::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final int value = result.getValue("value");


		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not set id-counter: No project opened.");
		} else {
			project.settings.idCounter.set(value);
		}

	}

}
