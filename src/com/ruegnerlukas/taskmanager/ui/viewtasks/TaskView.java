package com.ruegnerlukas.taskmanager.ui.viewtasks;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewmain.MainViewModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.TaskContent;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.TasksHeader;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TaskView extends AnchorPane implements MainViewModule {


	public static final String TITLE = "Tasks";

	@FXML private AnchorPane rootTaskView;
	@FXML private AnchorPane paneHeader;
	@FXML private SplitPane splitContent;
	@FXML private AnchorPane paneContent;
	@FXML private AnchorPane paneSidebar;

	private TasksHeader header;
	private TaskContent content;
	private TasksSidebar sidebar;




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
		header = new TasksHeader();
		AnchorUtils.setAnchors(header.getAnchorPane(), 0, 0, 0, 0);
		paneHeader.getChildren().add(header.getAnchorPane());

		// content
		content = new TaskContent();
		AnchorUtils.setAnchors(content.getAnchorPane(), 0, 0, 0, 0);
		paneContent.getChildren().add(content.getAnchorPane());

		// sidebar
		sidebar = new TasksSidebar();
		AnchorUtils.setAnchors(sidebar.getAnchorPane(), 0, 0, 0, 0);
		paneSidebar.getChildren().add(sidebar.getAnchorPane());

		// TODO TMP
		Task task = TaskLogic.createTask(Data.projectProperty.get());
		ProjectLogic.addTaskToProject(Data.projectProperty.get(), task);
		sidebar.setTask(task);


	}




	@Override
	public void onModuleClose() {
		header.dispose();
		content.dispose();
		sidebar.dispose();
	}




	@Override
	public void onModuleOpen() {

	}




	@Override
	public void onModuleSelected() {

	}




	@Override
	public void onModuleDeselected() {

	}


}
