package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import javafx.scene.paint.Color;

import java.util.*;

public class HelpCommand {


	public static Command create(List<Command> commands) {
		return new CommandBuilder()
				.text("help")
				.setDescription("Prints all commands and their description")
				.setExecutor(result -> HelpCommand.onCommand(result, commands))
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result, List<Command> allCommands) {

		// sort commands in buckets by primary identifier
		Map<String, List<Command>> cmdBuckets = new HashMap<>();
		for (Command cmd : allCommands) {
			String primaryIdentifier = cmd.identifier.identifier.get(0);
			if (!cmdBuckets.containsKey(primaryIdentifier)) {
				cmdBuckets.put(primaryIdentifier, new ArrayList<>());
			}
			cmdBuckets.get(primaryIdentifier).add(cmd);
		}


		// print help
		List<String> keys = new ArrayList<>(cmdBuckets.keySet());
		Collections.sort(keys);
		for (String primaryIdentifier : keys) {

			ConsoleWindowHandler.print(Color.CYAN, primaryIdentifier);

			List<Command> commands = cmdBuckets.get(primaryIdentifier);
			commands.sort((a, b) -> {
				String strA = String.join(" ", a.identifier.identifier);
				String strB = String.join(" ", b.identifier.identifier);
				return strA.compareToIgnoreCase(strB);
			});

			for (Command cmd : commands) {
				ConsoleWindowHandler.print("  " + cmd.string);
				ConsoleWindowHandler.print(Color.GRAY, "    -> " + cmd.description);
			}

			ConsoleWindowHandler.print("");
		}

	}


}
