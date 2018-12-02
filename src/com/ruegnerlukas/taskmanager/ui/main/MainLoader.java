package com.ruegnerlukas.taskmanager.ui.main;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewLoader;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainLoader implements IViewLoader {

	
	@Override
	public Parent load(Stage stage) {
		Logger.get().info("Loading WindowView:Main");
		ViewManager.openFXScene("view_main", stage, "ui/main/layout_view_main.fxml", 1280, 720, "TaskManager - Lukas Ruegner (2018)");
		return null;
	}
	

}
