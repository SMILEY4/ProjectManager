package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.SidebarItem;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TasksSidebar {


	private AnchorPane root;

	@FXML private AnchorPane paneBreadcrumb;
	@FXML private VBox boxAttributes;




	public TasksSidebar() {
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_SIDEBAR, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksSidebar-FXML: " + e);
		}
		create();
	}




	private void create() {
	}




	public void setTask(Task task) {

		boxAttributes.getChildren().clear();

		if(task != null) {
			List<TaskAttribute> attributes = Data.projectProperty.get().data.attributes;
			for (TaskAttribute attribute : attributes) {
				SidebarItem item = SidebarItem.createItem(attribute, task);
				if(item != null) {
					boxAttributes.getChildren().add(item);
				}
			}
		}

	}




	public AnchorPane getAnchorPane() {
		return this.root;
	}


}
