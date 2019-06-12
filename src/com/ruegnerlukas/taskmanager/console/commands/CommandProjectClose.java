package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;

public class CommandProjectClose {


	public static Command create() {
		return new CommandBuilder()
				.text("project")
				.text("close")
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Closes the current project. Add -l or --use-logic to use the rename-function of the Logic-Classes.")
				.setExecutor(CommandProjectClose::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final boolean useLogic = result.hasValue("use-logic");
		if(useLogic) {
			ProjectLogic.closeCurrentProject();
		} else {
			Data.projectProperty.set(null);
		}
	}


}
