package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewLoader;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class TasksLoader implements IViewLoader {

	@Override
	public Parent load(Stage stage) {
		Logger.get().info("Loading ViewModule:Tasks");
		ViewManager.loadFXModule("view_tasks", "ui/taskview/layout_view_tasks.fxml");
		return ViewManager.getRoot("view_tasks");
	}

}
