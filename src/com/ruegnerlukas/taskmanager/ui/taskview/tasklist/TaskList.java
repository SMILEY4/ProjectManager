package com.ruegnerlukas.taskmanager.ui.taskview.tasklist;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.ui.taskview.TaskView;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.TaskCard;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.scrollpane.ScrollPaneUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TaskList extends AnchorPane {


	public String title;
	public List<Task> tasks;

	public TaskView parent;
	@FXML private ScrollPane scrollTasks;
	@FXML private Label labelTitle;
	@FXML private VBox boxCards;




	public TaskList(String title, List<Task> tasks, TaskView parent) {
		this.title = title;
		this.tasks = tasks;
		this.parent = parent;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_tasklist.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskList-FXML: " + e);
		}

		this.setPrefSize(320, 100000);

		setupListeners();
		create();

	}




	private void create() {

		// title
		labelTitle.setText(title);


		// tasks
		for (Task task : tasks) {
			TaskCard card = new TaskCard(task, this);
			boxCards.getChildren().add(card);
		}

	}




	private void setupListeners() {
	}




	public void jumpToTask(Task task) {
		TaskCard card = findCard(task);
		if(card != null) {
			ScrollPaneUtils.centerContent(scrollTasks, card);
		}
	}




	public TaskCard findCard(Task task) {
		for (Node node : boxCards.getChildren()) {
			if (node instanceof TaskCard) {
				TaskCard card = (TaskCard) node;
				if (card.task == task) {
					return card;
				}
			}
		}
		return null;
	}




	public void dispose() {
		for (Node node : boxCards.getChildren()) {
			if (node instanceof TaskCard) {
				((TaskCard) node).dispose();
			}
		}
		EventManager.deregisterListeners(this);
	}

}
