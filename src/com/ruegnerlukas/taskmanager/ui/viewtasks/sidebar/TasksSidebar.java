package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.SidebarItem;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TasksSidebar {


	private AnchorPane root;

	@FXML private AnchorPane paneBreadcrumb;
	@FXML private VBox boxAttributes;


	private Task currentTask = null;

	private FXListChangeListener<TaskAttribute> listenerAttributes;




	public TasksSidebar() {
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_SIDEBAR, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksSidebar-FXML: " + e);
		}
		create();
	}




	private void create() {
		listenerAttributes = new FXListChangeListener<TaskAttribute>(Data.projectProperty.get().data.attributes) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskAttribute> c) {
				setTask(currentTask);
			}
		};
	}




	public void setTask(Task task) {

		this.currentTask = task;

		// remove prev. items
		for (Node node : boxAttributes.getChildren()) {
			if (node instanceof SidebarItem) {
				((SidebarItem) node).dispose();
			}
		}
		boxAttributes.getChildren().clear();

		if (task != null) {

			// add fixed attributes
			addAttribute(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION), task);
			addAttribute(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID), task);
			addAttribute(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.CREATED), task);
			addAttribute(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.LAST_UPDATED), task);
			addAttribute(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG), task);

			// add seperator
			boxAttributes.getChildren().add(new Separator());

			// add remaining/custom attributes
			List<TaskAttribute> attributes = Data.projectProperty.get().data.attributes;
			for (TaskAttribute attribute : attributes) {
				if (!attribute.type.get().fixed) {
					addAttribute(attribute, task);
				}
			}
		}

	}




	private void addAttribute(TaskAttribute attribute, Task task) {
		addAttribute(boxAttributes.getChildren().size(), attribute, task);
	}




	private void addAttribute(int index, TaskAttribute attribute, Task task) {
		if (attribute != null) {
			SidebarItem item = SidebarItem.createItem(attribute, task);
			if (item != null) {
				item.setOnAttribTypeChanged(event -> onAttributeTypeChanged(item));
				boxAttributes.getChildren().add(index, item);
			}
		}
	}




	private void onAttributeTypeChanged(SidebarItem item) {
		final int index = boxAttributes.getChildren().indexOf(item);
		item.dispose();
		boxAttributes.getChildren().remove(item);
		addAttribute(index, item.getAttribute(), item.getTask());
	}




	private SidebarItem findItem(TaskAttribute attribute) {
		for (Node node : boxAttributes.getChildren()) {
			if (node instanceof SidebarItem) {
				SidebarItem item = (SidebarItem) node;
				if (item.getAttribute() == attribute) {
					return item;
				}
			}
		}
		return null;
	}




	public AnchorPane getAnchorPane() {
		return this.root;
	}




	public void dispose() {
		listenerAttributes.removeFromAll();
	}


}
