package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskGroup;
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




	/**
	 * Tries to center the {@link TaskCard} of the given {@link Task} in this list.
	 */
	public void jumpToTask(Task task) {
		TaskCard card = findCard(task);
		if (card != null) {
			ScrollPaneUtils.centerContent(scrollTasks, card);
		}
	}




	/**
	 * Adds the given {@link Task} as a new {@link TaskCard} to this list.
	 */
	private void addTaskCard(Task task) {
		TaskCard card = new TaskCard(task, this);
		boxCards.getChildren().add(taskGroup.tasks.indexOf(task), card);
	}




	/**
	 * Removes the {@link TaskCard} of the given {@link Task} from this list.
	 */
	private void removeTaskCard(Task task) {
		TaskCard card = findCard(task);
		if (card != null) {
			card.dispose();
			boxCards.getChildren().remove(card);
		}
	}




	/**
	 * @return the {@link TaskCard} responsible for the given {@link Task} or null.
	 */
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




	/**
	 * @return the {@link TaskGroup} handled by this list.
	 */
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
