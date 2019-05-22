package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.TaskView;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.card.TaskCard;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.ScrollPaneUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class TaskContent {


	private TaskView taskView;

	private AnchorPane root;
	@FXML private HBox boxTasks;
	@FXML private Label labelHideSidebar;
	@FXML private ScrollPane scrollTasks;

	private FXListChangeListener<TaskGroup> listenerLastTaskGroups;
	private FXChangeListener<Boolean> listenerValid;

	public final CustomProperty<Task> selectedTask = new CustomProperty<>();




	public TaskContent(TaskView taskView) {
		try {
			this.taskView = taskView;
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_CONTENT, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskContent-FXML: " + e);
		}
		create();
	}




	private void create() {

		// listener: task-groups changed
		listenerLastTaskGroups = new FXListChangeListener<TaskGroup>(Data.projectProperty.get().temporaryData.lastTaskGroups) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskGroup> c) {
				if (Data.projectProperty.get().temporaryData.lastGroupsValid.get()) {

					for (TaskGroup group : getAllAdded(c)) {
						addTaskList(group);
					}
					for (TaskGroup group : getAllRemoved(c)) {
						removeTaskList(group);
					}
					for (ListChangeListener.Change<? extends TaskGroup> permutation : getAllPermutations(c)) {
						int[] p = new int[permutation.getTo() - permutation.getFrom()];
						for (int i = 0; i < p.length; i++) {
							p[i] = permutation.getPermutation(i + permutation.getFrom());
						}
						ArrayUtils.applyPermutation(boxTasks.getChildren(), p, permutation.getFrom());
					}
				}
			}
		};


		// listener: task-groups valid
		listenerValid = new FXChangeListener<Boolean>(Data.projectProperty.get().temporaryData.lastGroupsValid) {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					rebuildTaskLists();
				}
			}
		};


		rebuildTaskLists();
	}




	/**
	 * click on taskcard
	 */
	public static final int SELECTION_TASKCARD = 0;

	/**
	 * click on task in breadcrumb
	 */
	public static final int SELECTION_BREADCRUMB = 1;

	/**
	 * click on link to a task anywhere (e.g. dependencyItem)
	 */
	public static final int SELECTION_LINK = 2;




	public void selectTask(Task task, int selectionType) {

		// deselect last card
		if (selectedTask.get() != null) {
			TaskCard lastCard = findCard(selectedTask.get());
			if (lastCard != null) {
				lastCard.deselect();
			}
		}


		// select task + other actions by type
		if (selectionType == SELECTION_TASKCARD) {
			taskView.getSidebar().getBreadcrumbBar().clearTasks();
			if (task != null) {
				setSelectedTask(task);
				taskView.getSidebar().getBreadcrumbBar().pushTask(task);
			}
		}

		if (selectionType == SELECTION_BREADCRUMB) {
			if(task != null) {
				setSelectedTask(task);
				jumpToTask(task);
			}
		}

		if (selectionType == SELECTION_LINK) {
			setSelectedTask(task);
			jumpToTask(task);
			taskView.getSidebar().getBreadcrumbBar().pushTask(task);
		}

	}




	private void setSelectedTask(Task task) {
		if (task != null) {
			TaskCard card = findCard(task);
			if (card != null) {
				card.select();
			}
			selectedTask.set(task);
		}
		taskView.getSidebar().setTask(task);
	}




	public void jumpToTask(Task task) {
		TaskList list = findList(task);
		if (list != null) {
			ScrollPaneUtils.centerContent(scrollTasks, list);
			list.jumpToTask(task);
		}
	}




	private void rebuildTaskLists() {
		removeAllLists();
		List<TaskGroup> taskGroups = TaskDisplayLogic.createTaskGroups(Data.projectProperty.get());
		for (int i = 0; i < taskGroups.size(); i++) {
			TaskGroup group = taskGroups.get(i);
			addTaskList(group);
		}
	}




	private void addTaskList(TaskGroup group) {
		TaskList list = new TaskList(group, this);
		boxTasks.getChildren().add(list);
	}




	private void removeAllLists() {
		for (Node node : boxTasks.getChildren()) {
			if (!(node instanceof TaskList)) {
				continue;
			}
			((TaskList) node).dispose();
		}
		boxTasks.getChildren().clear();
	}




	private void removeTaskList(TaskList list) {
		list.dispose();
		boxTasks.getChildren().remove(list);
	}




	private void removeTaskList(TaskGroup group) {
		TaskList list = findList(group);
		if (list != null) {
			removeTaskList(list);
		}
	}




	private TaskList findList(TaskGroup group) {
		for (Node node : boxTasks.getChildren()) {
			if (!(node instanceof TaskList)) {
				continue;
			}
			TaskList list = (TaskList) node;
			if (list.getTaskGroup() == group) {
				return list;
			}
		}
		return null;
	}




	private TaskList findList(Task task) {
		for (Node node : boxTasks.getChildren()) {
			if (!(node instanceof TaskList)) {
				continue;
			}
			TaskList list = (TaskList) node;
			if (list.findCard(task) != null) {
				return list;
			}
		}
		return null;
	}




	private TaskCard findCard(Task task) {
		TaskList list = findList(task);
		if (list != null) {
			return list.findCard(task);
		} else {
			return null;
		}
	}




	public AnchorPane getAnchorPane() {
		return this.root;
	}




	public void dispose() {
		for (Node node : boxTasks.getChildren()) {
			if (node instanceof TaskList) {
				TaskList list = (TaskList) node;
				list.dispose();
			}
		}
		listenerLastTaskGroups.removeFromAll();
		listenerValid.removeFromAll();
	}

}
