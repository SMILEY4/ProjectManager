package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;

public class CommandProjectCreate {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("create")
				.optionalVariable("name", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Creates a new project with the given name. Add -l or --use-logic to use the rename-function of the Logic-Classes.")
				.setExecutor(CommandProjectCreate::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		// TODO
//		final String name = result.getValueOrDefault("name", "New Project");
//		final boolean useLogic = result.hasValue("use-logic");
//
//		Project project = ProjectLogic.createNewLocalProject(name);
//		if(useLogic) {
//			ProjectLogic.setCurrentProject(project);
//		} else {
//			Data.projectProperty.set(project);
//		}

	}


}
