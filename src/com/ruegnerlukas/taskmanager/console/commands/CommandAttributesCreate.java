package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandAttributesCreate {


	public static Command create() {

		String[] strType = new String[AttributeType.values().length];
		for(int i=0; i<AttributeType.values().length; i++) {
			strType[i] = AttributeType.values()[i].toString().toLowerCase();
		}

		return new CommandBuilder()
				.text("attributes")
				.text("create")
				.variable("name", String.class)
				.alternative("type", strType)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.optionalAlternative("force", "-f", "--force")
				.setDescription("Creates an attribute with the given name and type and adds it to the project.")
				.setExecutor(CommandAttributesCreate::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");
		final String strType = result.getValue("type");
		final boolean force = result.hasValue("force");
		final boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not create attribute: No project opened.");
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

			if(!force) {
				if(AttributeLogic.findAttribute(project, name) != null) {
					ConsoleWindowHandler.print(Color.RED, "Could not create attribute: Attribute with name \"" + name + "\" already exists.");
					return;
				}
				if(type.fixed) {
					ConsoleWindowHandler.print(Color.RED, "Could not create attribute: the type \"" + type.toString().toLowerCase() + "\" is a fixed type.");
					return;
				}
			}

			TaskAttribute attribute = AttributeLogic.createTaskAttribute(type, name, project);

			if(useLogic) {
				ProjectLogic.addAttributeToProject(project, attribute);
			} else {
				project.data.attributes.add(attribute);
			}

		}
	}


}
