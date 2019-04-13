package com.ruegnerlukas.taskmanager.ui.viewtasks;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TasksContent {


	private AnchorPane root;


	public TasksContent() {
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_CONTENT, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksContent-FXML: " + e);
		}
	}






	public AnchorPane getAnchorPane() {
		return this.root;
	}



}
