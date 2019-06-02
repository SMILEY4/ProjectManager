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
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DescriptionValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandTasksList {


	public static Command create() {
		return new CommandBuilder()
				.text("tasks")
				.text("list")
				.setDescription("Lists all tasks of the opened project.")
				.setExecutor(CommandTasksList::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not list tasks: No project opened.");
		} else {

			ConsoleWindowHandler.print("Tasks:");

			TaskAttribute attributeID = AttributeLogic.findAttribute(project, AttributeType.ID);
			TaskAttribute attributeDescr = AttributeLogic.findAttribute(project, AttributeType.DESCRIPTION);

			for(Task task : project.data.tasks) {

				IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(task, attributeID);
				DescriptionValue valueDescr = (DescriptionValue) TaskLogic.getValueOrDefault(task, attributeDescr);

				int id = valueID.getValue();
				String descr = valueDescr.getValue();
				if(descr.length() > 32) {
					descr = descr.substring(0, 32) + "...";
				}

				ConsoleWindowHandler.print(Color.GRAY, "T-" + id + ":  \"" + descr + "\"");

			}

		}
	}


}
