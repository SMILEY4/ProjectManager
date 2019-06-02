package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import javafx.scene.paint.Color;

public class CommandProjectRename {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("rename")
				.variable("name", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Renames the current project. Add -l or --use-logic to use the rename-function of the Logic-Classes.")
				.setExecutor(CommandProjectRename::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");
		final boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not rename project: No project opened.");
		} else {
			if(useLogic) {
				ProjectLogic.renameProject(project, name);
			} else {
				project.settings.name.set(name);
			}
		}
	}


}
