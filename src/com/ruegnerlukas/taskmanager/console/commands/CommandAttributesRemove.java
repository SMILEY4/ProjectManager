package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

public class CommandAttributesRemove {


	public static Command create() {

		return new CommandBuilder()
				.text("attributes")
				.text("remove")
				.variable("name", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.optionalAlternative("force", "-f", "--force")
				.setDescription("Removes the attribute with the given name.")
				.setExecutor(CommandAttributesRemove::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String name = result.getValue("name");
		final boolean force = result.hasValue("force");
		final boolean useLogic = result.hasValue("use-logic");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not remove attribute: No project opened.");

		} else {
			TaskAttribute attribute = AttributeLogic.findAttributeByName(project, name);
			if(attribute == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not remove attribute: No attribute with name \"" + name + "\" found.");

			} else {
				if(attribute.type.get().fixed && !force) {
					ConsoleWindowHandler.print(Color.RED, "Could not remove attribute: Attribute \"" + attribute.name.get() + "\" is fixed attribute.");

				} else {
					if(useLogic) {
						ProjectLogic.removeAttributeFromProject(project, attribute);

					} else {
						project.data.attributes.remove(attribute);
					}

				}

			}

		}
	}


}
