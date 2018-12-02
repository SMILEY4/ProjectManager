package com.ruegnerlukas.taskmanager;

import com.ruegnerlukas.simpleutils.JarLocation;
import com.ruegnerlukas.simpleutils.SystemUtils;
import com.ruegnerlukas.simpleutils.logging.LogLevel;
import com.ruegnerlukas.simpleutils.logging.builder.DefaultMessageBuilder;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.main.MainLoader;
import com.ruegnerlukas.taskmanager.ui.main.MainService;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.ProjectSettingsLoader;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.ProjectSettingsService;
import com.ruegnerlukas.taskmanager.ui.taskview.TasksLoader;
import com.ruegnerlukas.taskmanager.ui.taskview.TasksService;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ModuleView;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import com.ruegnerlukas.taskmanager.utils.viewsystem.WindowView;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TaskManager extends Application {

	
	
	public static void main(String[] args) {
		
		// setup logger
		Logger.get().redirectStdOutput(LogLevel.DEBUG, LogLevel.ERROR);
		((DefaultMessageBuilder)Logger.get().getMessageBuilder()).setSourceNameSizeMin(23);
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
		Logger.get().info("System information:   JAVA = " + SystemUtils.getJavaRuntimeName() +" "+ SystemUtils.getJavaVersion() + ",   OS = " + SystemUtils.getOSName());
	    launch(args);
		
	}
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ViewManager.setPrimaryStage(primaryStage);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent event) {
				closeApplication();
			} 
		});
		
		ViewManager.addView(new WindowView("view_main", new MainLoader(), new MainService()));
		ViewManager.addView(new ModuleView("view_projectsettings", new ProjectSettingsLoader(), new ProjectSettingsService()));
		ViewManager.addView(new ModuleView("view_tasks", new TasksLoader(), new TasksService()));
		ViewManager.getLoader("view_main").load(primaryStage);
		
	}
	
	
	
	
	public void closeApplication() {
		System.exit(0);
	}
	
	
}
