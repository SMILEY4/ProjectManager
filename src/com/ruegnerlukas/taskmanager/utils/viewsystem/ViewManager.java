package com.ruegnerlukas.taskmanager.utils.viewsystem;

import javafx.stage.Stage;

public class ViewManager {

	private static Stage primaryStage;


	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}
	

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

}
