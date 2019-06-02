package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import javafx.scene.paint.Color;

public class CommandAttributesList {


	public static Command create() {

		String[] strSelector = new String[AttributeType.values().length+1];
		strSelector[0] = "all";
		for(int i=0; i<AttributeType.values().length; i++) {
			strSelector[i+1] = AttributeType.values()[i].toString().toLowerCase();
		}

		return new CommandBuilder()
				.text("attributes")
				.text("list")
				.optionalAlternative("selector", strSelector)
				.setDescription("Returns a list of all attributes (of the given type) of the opened project.")
				.setExecutor(CommandAttributesList::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String selector = result.getValueOrDefault("selector", "all");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not list attributes: No project opened.");
		} else {

			ConsoleWindowHandler.print("Attributes" + (result.getValue("selector") == null ? ":" : (" (" + result.getValue("selector") + "):") ) );

			int nAttribs = 0;
			for (TaskAttribute attribute : project.data.attributes) {
				if("all".equalsIgnoreCase(selector)) {
					ConsoleWindowHandler.print(Color.GRAY, "  - " + attribute.name.get() + " (" + attribute.type.get() + (attribute.type.get().fixed ? ",fixed" : "") + ")");
					nAttribs++;
				} else {
					if(attribute.type.get().toString().equalsIgnoreCase(selector)) {
						ConsoleWindowHandler.print(Color.GRAY, "  - " + attribute.name.get() + " (" + attribute.type.get() + (attribute.type.get().fixed ? ",fixed" : "") + ")");
						nAttribs++;
					}
				}
			}

			if(nAttribs == 0) {
				ConsoleWindowHandler.print(Color.GRAY, "    (No Attributes)");
			}
			ConsoleWindowHandler.print("");

		}
	}


}
