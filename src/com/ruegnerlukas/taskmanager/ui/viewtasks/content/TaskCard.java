package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TaskCard extends AnchorPane {


	private Task task;
	protected TaskList parent;

	@FXML private Pane paneFlag;
	@FXML private Label labelID;
	@FXML private HBox boxIcons;
	@FXML private Label labelDesc;
	@FXML private VBox boxAttribs;

	private EventHandler<ActionEvent> handlerChangedFlag;
	private EventHandler<ActionEvent> handlerChangedDescription;




	public TaskCard(Task task, TaskList list) {
		this.task = task;
		this.parent = list;
		try {
			AnchorPane root = (AnchorPane) UIDataHandler.loadFXML(UIModule.ELEMENT_TASKCARD, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskCard-FXML: " + e);
		}
		create();
	}




	private void create() {
		this.setPrefSize(320, 200);

		this.setOnMouseClicked(event -> {
			parent.parent.selectTask(this.task);
		});

		handlerChangedFlag = e -> onFlagChanged();
		handlerChangedDescription = e -> onDescriptionChanged();
		task.addOnChange(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG), handlerChangedFlag);
		task.addOnChange(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION), handlerChangedDescription);

		final int id = (Integer) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID));
		final TaskFlag flag = (TaskFlag) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG));
		final String descr = (String) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION));

		labelID.setText("T-" + id);
		labelDesc.setText(descr);
		paneFlag.setStyle("-fx-background-color: " + flag.color.get().asHex());

	}




	public void select() {
		this.setStyle("-fx-border-color: #5a92ff; -fx-border-width: 3;");
	}




	public void deselect() {
		this.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
	}




	private void onFlagChanged() {
		final TaskFlag newFlag = (TaskFlag) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG));
		paneFlag.setStyle("-fx-background-color: " + newFlag.color.get().asHex());
	}




	private void onDescriptionChanged() {
		final String newDescr = (String) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION));
		labelDesc.setText(newDescr);
	}




	public Task getTask() {
		return this.task;
	}




	public void dispose() {
		task.removeHandler(handlerChangedFlag);
		task.removeHandler(handlerChangedDescription);
	}


}
