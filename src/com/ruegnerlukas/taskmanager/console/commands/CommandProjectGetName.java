package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import javafx.scene.paint.Color;

public class CommandProjectGetName {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("get")
				.text("name")
				.setDescription("Returns the name of the opened project.")
				.setExecutor(CommandProjectGetName::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {
		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "No project opened.");
		} else {
			ConsoleWindowHandler.print("Name of Project: \"" + project.settings.name.get() + "\"");
		}
	}


}
