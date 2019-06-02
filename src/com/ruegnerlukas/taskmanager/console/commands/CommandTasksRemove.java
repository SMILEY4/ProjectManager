package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandTasksRemove {


	public static Command create() {
		return new CommandBuilder()
				.text("tasks")
				.text("remove")
				.variable("id", Integer.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.setDescription("Removes the given task from the opened project.")
				.setExecutor(CommandTasksRemove::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		int id = result.getValue("id");
		boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not remove task: No project opened.");

		} else {

			Task task = null;

			TaskAttribute attributeID = AttributeLogic.findAttribute(project, AttributeType.ID);
			for (Task t : project.data.tasks) {
				IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(t, attributeID);
				if (valueID.getValue() == id) {
					task = t;
					break;
				}
			}

			if (task == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not remove task: Task T-" + id + " not found.");

			} else {
				if (useLogic) {
					TaskLogic.deleteTask(project, task);
				} else {
					project.data.tasks.remove(task);
				}
			}

		}
	}


}
