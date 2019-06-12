package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandTaskInfo {


	public static Command create() {
		return new CommandBuilder()
				.text("task")
				.text("info")
				.variable("id", Integer.class)
				.setDescription("Returns all available information about the given task.")
				.setExecutor(CommandTaskInfo::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final int id = result.getValue("id");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not find information: No project opened.");
		} else {

			TaskAttribute attributeID = AttributeLogic.findAttribute(project, AttributeType.ID);

			Task task = null;
			for (Task t : project.data.tasks) {
				int tID = ((IDValue) TaskLogic.getValueOrDefault(t, attributeID)).getValue();
				if (tID == id) {
					task = t;
					break;
				}
			}


			if (task == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not find task: No task with id \"" + id + "\" found.");
			} else {

				ConsoleWindowHandler.print("Task T-" + id + ":");
				for (TaskAttribute attribute : project.data.attributes) {

					TaskValue<?> value = TaskLogic.getValueOrDefault(task, attribute);

					if (value.getAttType() == null) {
						ConsoleWindowHandler.print(Color.GRAY, attribute.name.get() + " = " + " - ");
					} else {
						ConsoleWindowHandler.print(Color.GRAY, attribute.name.get() + " = " + value.getValue().toString());
					}

				}
				ConsoleWindowHandler.print("");

			}

		}
	}


}
