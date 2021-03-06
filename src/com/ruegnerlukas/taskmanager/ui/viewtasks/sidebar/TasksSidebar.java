package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.TaskView;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.TaskContent;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.SidebarItem;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TasksSidebar {


	private TaskView taskView;

	private AnchorPane root;
	@FXML private AnchorPane paneBreadcrumb;
	@FXML private VBox boxAttributes;
	@FXML private Button btnDeleteTask;
	private BreadcrumbBar breadcrumbBar;

	private Task currentTask = null;
	private FXListChangeListener<TaskAttribute> listenerAttributes;
	private FXListChangeListener<Task> listenerTasks;




	public TasksSidebar(TaskView taskView) {
		this.taskView = taskView;
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_SIDEBAR, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksSidebar-FXML.", e);
		}
		create();
	}




	private void create() {

		root.getStyleClass().add("tasks-sidebar");
		boxAttributes.getStyleClass().add("attribute-box");
		btnDeleteTask.getStyleClass().add("delete-button");


		// BREADCRUMB
		breadcrumbBar = new BreadcrumbBar() {
			@Override
			public boolean onStepBack(Task task) {
				taskView.getContent().selectTask(task, TaskContent.SELECTION_BREADCRUMB);
				return true;
			}




			@Override
			public boolean onJumpBack(Task task) {
				taskView.getContent().selectTask(task, TaskContent.SELECTION_BREADCRUMB);
				return true;
			}
		};
		AnchorUtils.setAnchors(breadcrumbBar, 0, 0, 0, 0);
		paneBreadcrumb.getChildren().add(breadcrumbBar);


		// LISTENERS
		listenerAttributes = new FXListChangeListener<TaskAttribute>(Data.projectProperty.get().data.attributes) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskAttribute> c) {
				if (currentTask != null) {
					if (getAllAdded(c).isEmpty()) {
						for (TaskAttribute attribute : getAllRemoved(c)) {
							removeAttribute(attribute);
						}
					} else {
						setTask(currentTask);
					}
				}
			}
		};

		listenerTasks = new FXListChangeListener<Task>(Data.projectProperty.get().data.tasks) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Task> c) {
				List<Task> removed = getAllRemoved(c);
				if (removed.contains(currentTask)) {
					setTask(null);
					breadcrumbBar.clearTasks();
				}
			}
		};

		btnDeleteTask.setOnAction(e -> {
			if (currentTask != null) {
				ProjectLogic.removeTaskFromProject(Data.projectProperty.get(), currentTask);
			}
		});
	}




	/**
	 * Shows the content of the given {@link Task} in the sidebar.
	 */
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

			Project project = Data.projectProperty.get();

			// add fixed values
			addAttribute(AttributeLogic.findAttributeByType(project, AttributeType.DESCRIPTION), task);
			addAttribute(AttributeLogic.findAttributeByType(project, AttributeType.ID), task);
			addAttribute(AttributeLogic.findAttributeByType(project, AttributeType.CREATED), task);
			addAttribute(AttributeLogic.findAttributeByType(project, AttributeType.LAST_UPDATED), task);
			addAttribute(AttributeLogic.findAttributeByType(project, AttributeType.FLAG), task);

			// add separator
			boxAttributes.getChildren().add(new Separator());

			// add remaining/custom values
			List<TaskAttribute> attributes = project.data.attributes;
			for (TaskAttribute attribute : attributes) {
				if (!attribute.type.get().fixed) {
					addAttribute(attribute, task);
				}
			}
		}

	}




	/**
	 * Removes item of the given {@link TaskAttribute} from the sidebar
	 */
	private void removeAttribute(TaskAttribute attribute) {
		SidebarItem item = findItem(attribute);
		if (item != null) {
			item.dispose();
			boxAttributes.getChildren().remove(item);
		}
	}




	/**
	 * Adds a new item for the given {@link TaskAttribute} and {@link Task} to the sidebar
	 */
	private void addAttribute(TaskAttribute attribute, Task task) {
		addAttribute(boxAttributes.getChildren().size(), attribute, task);
	}




	/**
	 * Adds a new item for the given {@link TaskAttribute} and {@link Task} at the given index to the sidebar
	 */
	private void addAttribute(int index, TaskAttribute attribute, Task task) {
		if (attribute != null) {
			SidebarItem item = SidebarItem.createItem(this, attribute, task);
			if (item != null) {
				item.setOnAttribTypeChanged(event -> onAttributeTypeChanged(item));
				boxAttributes.getChildren().add(index, item);
			}
		}
	}




	/**
	 * Changes/Updates the {@link AttributeType} of the given {@link SidebarItem} by removing it and adding a new {@link SidebarItem} with the new {@link AttributeType}.
	 */
	private void onAttributeTypeChanged(SidebarItem item) {
		final int index = boxAttributes.getChildren().indexOf(item);
		item.dispose();
		boxAttributes.getChildren().remove(item);
		addAttribute(index, item.getAttribute(), item.getTask());
	}




	/**
	 * @return the {@link SidebarItem} responsible for the given {@link TaskAttribute} or null.
	 */
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




	public BreadcrumbBar getBreadcrumbBar() {
		return breadcrumbBar;
	}




	public TaskView getTaskView() {
		return taskView;
	}




	public void dispose() {
		listenerAttributes.removeFromAll();
		listenerTasks.removeFromAll();
	}


}
