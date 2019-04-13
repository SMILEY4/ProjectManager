package com.ruegnerlukas.taskmanager.ui.viewtasks;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TasksSidebar {


	private AnchorPane root;


	public TasksSidebar() {
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_SIDEBAR, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksSidebar-FXML: " + e);
		}
	}






	public AnchorPane getAnchorPane() {
		return this.root;
	}


}
