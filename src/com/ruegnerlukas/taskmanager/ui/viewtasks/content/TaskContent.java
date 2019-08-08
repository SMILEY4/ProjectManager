package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.TaskView;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.card.TaskCard;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.ScrollPaneUtils;
import javafx.application.Platform;
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

	private final CustomProperty<Task> selectedTask = new CustomProperty<>();




	public TaskContent(TaskView taskView) {
		try {
			this.taskView = taskView;
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_CONTENT, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskContent-FXML.", e);
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
						reselectTask();
					}
					for (TaskGroup group : getAllRemoved(c)) {
						removeTaskList(group);
					}
					for (ListChangeListener.Change<? extends TaskGroup> permutation : getAllPermutations(c)) {
						applyPermutation(boxTasks.getChildren(), permutation);
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




	/**
	 * Selects and jumpts to the given {@link Task}. The type of selection can be specified. <br>
	 * 0 = SELECTION_TASKCARD: usually by clicking on a {@link TaskCard}; Replaces the tasks in the breadcrumb-bar with the selected task.
	 * 1 = SELECTION_BREADCRUMB: usually by clicking on a link to the task in the breadcrumb-bar. this will not add the selected task to the breadcrumb-bar.
	 * 2 = SELECTION_LINK: usually by clicking on a link to a the task. This will add the selected task to the end of the breadcrumb--bar.
	 */
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
			if (task != null) {
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




	/**
	 * Deselects the previously selected {@link Task} and selects the given {@link Task}.
	 */
	private void setSelectedTask(Task task) {
		if (task != null) {
			TaskCard card = findCard(task);
			if (card != null) {
				card.select();
			}
			selectedTask.set(task);
		}
		if (taskView.getSidebar() != null) {
			taskView.getSidebar().setTask(task);
		}
	}




	/**
	 * Refreshes the selection and jumps to the selected task.
	 */
	public void reselectTask() {
		setSelectedTask(selectedTask.get());
		if (selectedTask.get() != null) {
			Platform.runLater(() -> jumpToTask(selectedTask.get()));
		}
	}




	/**
	 * Tries to center the {@link TaskCard} of the given {@link Task} in the content-area.
	 */
	public void jumpToTask(Task task) {
		TaskList list = findList(task);
		if (list != null) {
			ScrollPaneUtils.centerContent(scrollTasks, list);
			list.jumpToTask(task);
		}
	}




	/**
	 * Removes all {@link TaskCard}s and {@link TaskList}s, creates new {@link TaskGroup}s and adds them to the content area
	 */
	private void rebuildTaskLists() {
		removeAllLists();
		List<TaskGroup> taskGroups = TaskDisplayLogic.createTaskGroups(Data.projectProperty.get());
		for (int i = 0; i < taskGroups.size(); i++) {
			TaskGroup group = taskGroups.get(i);
			addTaskList(group);
		}
		reselectTask();
	}




	/**
	 * Adds the given {@link TaskGroup} as a new {@link TaskList}.
	 */
	private void addTaskList(TaskGroup group) {
		TaskList list = new TaskList(group, this);
		boxTasks.getChildren().add(list);
	}




	/**
	 * Removes all {@link TaskList}s.
	 */
	private void removeAllLists() {
		for (Node node : boxTasks.getChildren()) {
			if (!(node instanceof TaskList)) {
				continue;
			}
			((TaskList) node).dispose();
		}
		boxTasks.getChildren().clear();
	}




	/**
	 * Removes the given {@link TaskList}.
	 */
	private void removeTaskList(TaskList list) {
		list.dispose();
		boxTasks.getChildren().remove(list);
	}




	/**
	 * Removes the {@link TaskList} for the given {@link TaskGroup}.
	 */
	private void removeTaskList(TaskGroup group) {
		TaskList list = findList(group);
		if (list != null) {
			removeTaskList(list);
		}
	}




	/**
	 * @return the {@link TaskList} responsible for the given {@link TaskList} or null.
	 */
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




	/**
	 * @return the {@link TaskList} containing the given {@link Task} or null.
	 */
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




	/**
	 * @return the {@link TaskCard} responsible for the given {@link Task} or null.
	 */
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




	/**
	 * @return the "button" to show/hide the sidebar-area
	 */
	public Label getSidebarControlArea() {
		return labelHideSidebar;
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
