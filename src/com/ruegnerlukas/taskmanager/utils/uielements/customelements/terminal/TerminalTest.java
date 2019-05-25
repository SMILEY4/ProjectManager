package com.ruegnerlukas.taskmanager.utils.uielements.customelements.terminal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TerminalTest extends Application {


	public static void main(String[] args) {
		launch(args);
	}




	@Override
	public void start(Stage primaryStage) throws Exception {


		TerminalView canvas = new TerminalView();
		Scene scene = new Scene(canvas, 1280, 720);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
