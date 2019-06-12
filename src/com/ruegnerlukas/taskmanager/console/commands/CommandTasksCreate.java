package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import javafx.scene.paint.Color;

public class CommandTasksCreate {


	public static Command create() {
		return new CommandBuilder()
				.text("tasks")
				.text("create")
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Creates a new task and adds it to the opened project.")
				.setExecutor(CommandTasksCreate::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not create task: No project opened.");

		} else {

			Task task = TaskLogic.createTask(project);

			if (useLogic) {
				ProjectLogic.addTaskToProject(project, task);
			} else {
				project.data.tasks.add(task);
			}

		}
	}


}
