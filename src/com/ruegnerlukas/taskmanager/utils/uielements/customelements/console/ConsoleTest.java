package com.ruegnerlukas.taskmanager.utils.uielements.customelements.console;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConsoleTest extends Application {


	public static void main(String[] args) {
		launch(args);
	}








	@Override
	public void start(Stage primaryStage) throws Exception {

		ConsoleView console = new ConsoleView(10, 100);
		console.setInputListener((startText, strInput) -> {
			String[] tokens = strInput.split(" ");
			if (tokens[0].equals("echo")) {
				console.printLine("> " + tokens[1]);
			}
			if (tokens[0].equals("start")) {
				console.setStartText(tokens[1]);
			}
		});

		Scene scene = new Scene(console, 600, 500);

		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setScene(scene);
		primaryStage.show();

	}


}
