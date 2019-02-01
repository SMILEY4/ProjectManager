package com.ruegnerlukas.taskmanager.ui.taskview.tasklist;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.TaskCard;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TaskList extends AnchorPane {


	public String title;
	public List<Task> tasks;

	@FXML private Label labelTitle;
	@FXML private VBox boxCards;




	public TaskList(String title, List<Task> tasks) {
		this.title = title;
		this.tasks = tasks;

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
		for(Task task : tasks) {
			TaskCard card = new TaskCard(task);
			boxCards.getChildren().add(card);
		}

	}




	private void setupListeners() {
	}


}
