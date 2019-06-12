package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandAttributeInfo {


	public static Command create() {
		return new CommandBuilder()
				.text("attribute")
				.text("info")
				.variable("name", String.class)
				.setDescription("Returns all available information about the given attribute.")
				.setExecutor(CommandAttributeInfo::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");


		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not find information: No project opened.");
		} else {

			TaskAttribute attribute = AttributeLogic.findAttribute(project, name);
			if(attribute == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not find attribute: No attribute with name \"" + name + "\" found.");

			} else {
				ConsoleWindowHandler.print("Attribute \"" + attribute.name.get() + "\":");
				ConsoleWindowHandler.print(Color.GRAY, "  name = " + attribute.name.get());
				ConsoleWindowHandler.print(Color.GRAY, "  type = " + attribute.type.get());
				ConsoleWindowHandler.print(Color.GRAY, "  ------------------");
				for(AttributeValueType valueType : attribute.values.keySet()) {
					AttributeValue<?> value = attribute.values.get(valueType);
					ConsoleWindowHandler.print(Color.GRAY, "  " + valueType.toString().toLowerCase() + " = " + value.getValue());
				}
				ConsoleWindowHandler.print("");

			}

		}
	}


}
