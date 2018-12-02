package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewLoader;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class ProjectSettingsLoader implements IViewLoader {

	@Override
	public Parent load(Stage stage) {
		Logger.get().info("Loading ViewModule:ProjectSettings");
		ViewManager.loadFXModule("view_projectsettings", "ui/projectsettingsview/layout_view_projectsettings.fxml");
		return ViewManager.getRoot("view_projectsettings");
	}

}
