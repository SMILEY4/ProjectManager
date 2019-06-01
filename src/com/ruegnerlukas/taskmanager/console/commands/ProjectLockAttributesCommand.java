package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import javafx.scene.paint.Color;

public class ProjectLockAttributesCommand {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("lock-attributes")
				.variable("lock", Boolean.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Locks the attribute of the current project. Add -l or --use-logic to use the lock-function of the Logic-Classes.")
				.setExecutor(ProjectLockAttributesCommand::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {
		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not lock attributes: No project opened.");
		} else {
			final boolean locked = result.getValue("lock");
			final boolean useLogic = result.hasParameter("use-logic");
			if(useLogic) {
				ProjectLogic.lockTaskAttributes(project, locked);
			} else {
				project.settings.attributesLocked.set(locked);
			}
		}

	}

}
