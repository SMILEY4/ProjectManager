package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.DataChange;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import javafx.scene.paint.Color;

public class CommandDataSimulateExternalChange {


	public static Command create() {
		return new CommandBuilder()
				.text("data")
				.text("sim")
				.text("external")
				.variable("identifier", String.class)
				.alternative("datatype", "string", "boolean", "integer", "float", "double")
				.variable("value", String.class)
				.setDescription("Simulates a change of a given variable of the external data source")
				.setExecutor(CommandDataSimulateExternalChange::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String identifier = result.getValue("identifier");
		final String datatype = result.getValue("datatype");
		final String value = result.getValue("value");

		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not simulate ext. data change: No project opened.");
		} else {

			Object oldValue = null; // TODO ?

			Object newValue = null;

			try {
				switch (datatype) {
					case "string": {
						newValue = value;
						break;
					}
					case "boolean": {
						newValue = Boolean.parseBoolean(value);
						break;
					}
					case "integer": {
						newValue = Integer.parseInt(value);
						break;
					}
					case "float": {
						newValue = Float.parseFloat(value);
						break;
					}
					case "double": {
						newValue = Double.parseDouble(value);
						break;
					}
				}
			} catch (Exception e) {
				ConsoleWindowHandler.print(Color.RED, "Could not simulate ext. data change: Invalid value.");
			}

			DataChange change = new DataChange(identifier, oldValue, newValue);
			DataHandler.onExternalChange(change);

		}
	}


}
