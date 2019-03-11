package com.ruegnerlukas.taskmanager;

import com.ruegnerlukas.simpleutils.JarLocation;
import com.ruegnerlukas.simpleutils.SystemUtils;
import com.ruegnerlukas.simpleutils.logging.LogLevel;
import com.ruegnerlukas.simpleutils.logging.builder.DefaultMessageBuilder;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.main.MainView;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class TaskManager extends Application {


	public static void main(String[] args) {

		// setup logger
		Logger.get().redirectStdOutput(LogLevel.DEBUG, LogLevel.ERROR);
		((DefaultMessageBuilder) Logger.get().getMessageBuilder()).setSourceNameSizeMin(23);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				Logger.get().info("=========================================");
				Logger.get().blankLine();
				Logger.get().blankLine();
				Logger.get().blankLine();
				Logger.get().blankLine();
				Logger.get().close();
			}
		}, "Shutdown-thread"));

		// start application
		Logger.get().blankLine();
		Logger.get().info("Starting Application (" + JarLocation.getJarLocation(TaskManager.class));
		Logger.get().info("System information:   JAVA = " + SystemUtils.getJavaRuntimeName() + " " + SystemUtils.getJavaVersion() + ",   OS = " + SystemUtils.getOSName());
		launch(args);

	}




	@Override
	public void start(Stage primaryStage) {

		TaskManager.setPrimaryStage(primaryStage);
		primaryStage.setOnCloseRequest(event -> closeApplication());

		MainView viewMain = new MainView();
		Scene scene = new Scene(viewMain, 1280, 720);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
			if (ke.getCode() == KeyCode.R) {
				UIDataHandler.reloadAll();
				ke.consume();
			}
		});


		primaryStage.setScene(scene);
		primaryStage.show();


	}




	public void closeApplication() {
		System.exit(0);
	}




	private static Stage primaryStage;




	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}




	public static Stage getPrimaryStage() {
		return primaryStage;
	}


}
