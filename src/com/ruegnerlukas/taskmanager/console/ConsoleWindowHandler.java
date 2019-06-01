package com.ruegnerlukas.taskmanager.console;

import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.console.ConsoleView;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConsoleWindowHandler {


	private static ConsoleView console;
	private static Stage stage;




	public static ConsoleView getConsole() {
		return console;
	}




	public static void openNew() {
		close();
		console = new ConsoleView(5000, 200);
		console.setStartText("$ ");
		console.setInputListener((startText, strInput) -> CommandHandler.onCommand(strInput));
		stage = new Stage();
		stage.initModality(Modality.NONE);
		stage.initOwner(TaskManager.getPrimaryStage());
		stage.setOnCloseRequest(e -> close());
		Scene scene = new Scene(console, 500, 500);
		stage.setScene(scene);
		stage.show();
	}




	public static void close() {
		if (stage != null) {
			stage.close();
			console = null;
			stage = null;
		}
	}




	public static void print(String str) {
		System.out.println(str);
		if (getConsole() != null) {
			getConsole().printLine(str);
		}
	}


	public static void print(Color color, String str) {
		System.out.println(str);
		if (getConsole() != null) {
			getConsole().printLine(color, str);
		}
	}


}
