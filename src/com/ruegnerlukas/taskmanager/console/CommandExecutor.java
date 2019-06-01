package com.ruegnerlukas.taskmanager.console;

import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandresults.FailedCommandResult;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import javafx.scene.paint.Color;

public interface CommandExecutor {


	void onCommand(SuccessfulCommandResult result);


	default void onFailedCommand(FailedCommandResult result) {
		Command command = result.command;

		String strStart = "\"" + String.join(" ", command.identifier.identifier) + "\"" + " failed for input \"";
		ConsoleWindowHandler.print(Color.RED, strStart + result.inputString + "\"");

		StringBuilder strIndex = new StringBuilder();
		for (int i = 0; i < strStart.length() + result.index; i++) {
			strIndex.append(' ');
		}
		strIndex.append('^');
		ConsoleWindowHandler.print(Color.RED, strIndex.toString());

		for (String strError : result.errors) {
			ConsoleWindowHandler.print(Color.RED, "   " + strError);
		}


	}


}
