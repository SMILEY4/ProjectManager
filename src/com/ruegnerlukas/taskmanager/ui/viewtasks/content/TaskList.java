package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.card.TaskCard;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ScrollPaneUtils;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TaskList extends AnchorPane {


	private TaskGroup taskGroup;
	private FXListChangeListener<Task> listenerTasks;

	@FXML private Label labelTitle;
	@FXML private VBox boxCards;
	@FXML private ScrollPane scrollTasks;

	private TaskContent parent;




	public TaskList(TaskGroup taskGroup, TaskContent parent) {
		this.taskGroup = taskGroup;
		this.parent = parent;
		try {
			AnchorPane root = (AnchorPane) UIDataHandler.loadFXML(UIModule.ELEMENT_TASKLIST, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskList-FXML: " + e);
		}
		create();
	}




	private void create() {
		this.setMinSize(50, 50);
		this.setPrefWidth(330);
		this.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);


		listenerTasks = new FXListChangeListener<Task>(taskGroup.tasks) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Task> c) {
				for (Task task : getAllAdded(c)) {
					addTaskCard(task);
					parent.reselectTask();
				}
				for (Task task : getAllRemoved(c)) {
					removeTaskCard(task);
				}
				for (ListChangeListener.Change<? extends Task> permutation : getAllPermutations(c)) {
					applyPermutation(boxCards.getChildren(), permutation);
//					int[] p = new int[permuation.getTo() - permuation.getFrom()];
//					for (int i = 0; i < p.length; i++) {
//						p[i] = permuation.getPermutation(i + permuation.getFrom());
//					}
//					ArrayUtils.applyPermutation(boxCards.getChildren(), p, permuation.getFrom());
				}
			}
		};

		String title = TaskDisplayLogic.createTaskGroupTitle(Data.projectProperty.get(), taskGroup,
				(taskGroup.tasks.isEmpty() ? null : taskGroup.tasks.get(0)));
		labelTitle.setText(title);

		for (int i = 0; i < taskGroup.tasks.size(); i++) {
			addTaskCard(taskGroup.tasks.get(i));
		}

	}




	public TaskContent getTaskContent() {
		return parent;
	}




	public void jumpToTask(Task task) {
		TaskCard card = findCard(task);
		if (card != null) {
			ScrollPaneUtils.centerContent(scrollTasks, card);
		}
	}




	private void addTaskCard(Task task) {
		TaskCard card = new TaskCard(task, this);
		boxCards.getChildren().add(taskGroup.tasks.indexOf(task), card);
	}




	private void removeTaskCard(Task task) {
		TaskCard card = findCard(task);
		if (card != null) {
			card.dispose();
			boxCards.getChildren().remove(card);
		}
	}




	public TaskCard findCard(Task task) {
		for (Node node : boxCards.getChildren()) {
			if (!(node instanceof TaskCard)) {
				continue;
			}
			TaskCard card = (TaskCard) node;
			if (card.getTask() == task) {
				return card;
			}
		}
		return null;
	}




	public TaskGroup getTaskGroup() {
		return this.taskGroup;
	}




	public void dispose() {
		for (Node node : boxCards.getChildren()) {
			if (!(node instanceof TaskCard)) {
				continue;
			}
			TaskCard card = (TaskCard) node;
			card.dispose();
		}
		listenerTasks.removeFromAll();
	}

}
