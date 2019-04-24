package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TaskList extends AnchorPane {


	private TaskGroup taskGroup;

	@FXML private Label labelTitle;
	@FXML private VBox boxCards;




	public TaskList(TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
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

		taskGroup.tasks.addListener((ListChangeListener<Task>) c -> {
			while (c.next()) {
				if(c.wasPermutated()) {
					int[] p = new int[c.getTo()-c.getFrom()];
					for(int i=0; i<p.length; i++) {
						p[i] = c.getPermutation(i+c.getFrom());
					}
					ArrayUtils.applyPermutation(boxCards.getChildren(), p, c.getFrom());
				}
				if (c.wasAdded()) {
					for (Task task : c.getAddedSubList()) {
						addTaskCard(task);
					}
				}
				if (c.wasRemoved()) {
					for (Task task : c.getRemoved()) {
						removeTaskCard(task);
					}
				}
			}
		});

		labelTitle.setText("List n=" + taskGroup.tasks.size());

		for (int i = 0; i < taskGroup.tasks.size(); i++) {
			addTaskCard(taskGroup.tasks.get(i));
		}

	}




	private void addTaskCard(Task task) {
		TaskCard card = new TaskCard(task);
		boxCards.getChildren().add(taskGroup.tasks.indexOf(task), card);
	}




	private void removeTaskCard(Task task) {
		TaskCard card = findTaskCard(task);
		if (card != null) {
			card.dispose();
			boxCards.getChildren().remove(card);
		}
	}




	public TaskCard findTaskCard(Task task) {
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
	}

}
