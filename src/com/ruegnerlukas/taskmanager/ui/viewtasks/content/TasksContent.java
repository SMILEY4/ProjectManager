package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class TasksContent {


	private AnchorPane root;

	@FXML private HBox boxTasks;
	@FXML private Label labelHideSidebar;




	public TasksContent() {
		try {
			root = (AnchorPane) UIDataHandler.loadFXML(UIModule.VIEW_TASKS_CONTENT, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksContent-FXML: " + e);
		}
		create();
	}




	private void create() {

		Data.projectProperty.get().temporaryData.lastTaskGroups.addListener((ListChangeListener<TaskGroup>) c -> {
			if(Data.projectProperty.get().temporaryData.lastGroupsValid.get()) {
				while (c.next()) {
					if(c.wasPermutated()) {
						int[] p = new int[c.getTo()-c.getFrom()];
						for(int i=0; i<p.length; i++) {
							p[i] = c.getPermutation(i+c.getFrom());
						}
						ArrayUtils.applyPermutation(boxTasks.getChildren(), p, c.getFrom());
					}
					if (c.wasAdded()) {
						for (TaskGroup group : c.getAddedSubList()) {
							addTaskList(group);
						}
					}
					if (c.wasRemoved()) {
						for (TaskGroup group : c.getRemoved()) {
							removeTaskList(group);
						}
					}
				}
			}
		});

		Data.projectProperty.get().temporaryData.lastGroupsValid.addListener(((observable, oldValue, newValue) -> {
			if (!newValue) {
				rebuildTaskLists();
			}
		}));
		rebuildTaskLists();
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
		TaskList list = new TaskList(group);
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
			if (list.findTaskCard(task) != null) {
				return list;
			}
		}
		return null;
	}




	public AnchorPane getAnchorPane() {
		return this.root;
	}


}
