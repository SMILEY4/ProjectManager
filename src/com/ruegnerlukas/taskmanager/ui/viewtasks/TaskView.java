package com.ruegnerlukas.taskmanager.ui.viewtasks;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.TasksContent;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.TasksHeader;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TaskView extends AnchorPane {


	public static final String TITLE = "Tasks";

	@FXML private AnchorPane rootTaskView;
	@FXML private AnchorPane paneHeader;
	@FXML private SplitPane splitContent;
	@FXML private AnchorPane paneContent;
	@FXML private AnchorPane paneSidebar;




	public TaskView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_TASKS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskView-FXML: " + e);
		}
		create();
	}




	private void create() {

		// header
		TasksHeader header = new TasksHeader();
		AnchorUtils.setAnchors(header.getAnchorPane(), 0, 0, 0, 0);
		paneHeader.getChildren().add(header.getAnchorPane());

		// content
		TasksContent content = new TasksContent();
		AnchorUtils.setAnchors(content.getAnchorPane(), 0, 0, 0, 0);
		paneContent.getChildren().add(content.getAnchorPane());

		// sidebar
		TasksSidebar sidebar = new TasksSidebar();
		AnchorUtils.setAnchors(sidebar.getAnchorPane(), 0, 0, 0, 0);
		paneSidebar.getChildren().add(sidebar.getAnchorPane());

	}


}
