package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandAttributeType {


	public static Command create() {

		String[] strType = new String[AttributeType.values().length];
		for(int i=0; i<AttributeType.values().length; i++) {
			strType[i] = AttributeType.values()[i].toString().toLowerCase();
		}

		return new CommandBuilder()
				.text("attribute")
				.text("set")
				.text("type")
				.variable("name", String.class)
				.alternative("type", strType)
				.setDescription("Changes the type of the attribute with the given name.")
				.setExecutor(CommandAttributeType::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");
		final String strType = result.getValue("type");
		final boolean force = result.hasValue("force");


		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not change type: No project opened.");
		} else {

			TaskAttribute attribute = AttributeLogic.findAttribute(project, name);
			if(attribute == null) {
				ConsoleWindowHandler.print(Color.RED, "Could change type: No attribute with name \"" + name + "\" found.");

			} else {


				AttributeType type = null;
				switch (strType.toUpperCase()) {
					case "ID": {
						type = AttributeType.ID;
						break;
					}
					case "DESCRIPTION": {
						type = AttributeType.DESCRIPTION;
						break;
					}
					case "CREATED": {
						type = AttributeType.CREATED;
						break;
					}
					case "LAST_UPDATED": {
						type = AttributeType.LAST_UPDATED;
						break;
					}
					case "FLAG": {
						type = AttributeType.FLAG;
						break;
					}
					case "TEXT": {
						type = AttributeType.TEXT;
						break;
					}
					case "NUMBER": {
						type = AttributeType.NUMBER;
						break;
					}
					case "BOOLEAN": {
						type = AttributeType.BOOLEAN;
						break;
					}
					case "CHOICE": {
						type = AttributeType.CHOICE;
						break;
					}
					case "DATE": {
						type = AttributeType.DATE;
						break;
					}
					case "DEPENDENCY": {
						type = AttributeType.DEPENDENCY;
						break;
					}
				}

				AttributeLogic.setTaskAttributeType(project, attribute, type);
			}

		}
	}


}
