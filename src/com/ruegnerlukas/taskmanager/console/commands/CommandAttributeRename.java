package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandAttributeRename {


	public static Command create() {
		return new CommandBuilder()
				.text("attribute")
				.text("rename")
				.variable("name", String.class)
				.variable("new-name", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.optionalAlternative("force", "-f", "--force")
				.setDescription("Renames the attribute with the given name. Add -l or --use-logic to use the rename-function of the Logic-Classes.")
				.setExecutor(CommandAttributeRename::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");
		final boolean force = result.hasValue("force");
		final String newName = result.getValue("new-name");
		final boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if(project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not rename attribute: No project opened.");
		} else {

			TaskAttribute attribute = AttributeLogic.findAttribute(project, name);

			if(attribute == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not rename attribute: No attribute with name \"" + name + "\" found.");
			} else {

				if(attribute.type.get().fixed && !force) {
					ConsoleWindowHandler.print(Color.RED, "Could not rename attribute: Attribute \"" + attribute.name.get() + "\" is fixed attribute.");

				} else {
					if(useLogic) {
						AttributeLogic.renameTaskAttribute(project, attribute, newName);
					} else {
						attribute.name.set(newName);
					}
				}
			}

		}
	}


}
