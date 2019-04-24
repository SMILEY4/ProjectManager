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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TaskCard extends AnchorPane {


	private Task task;

	@FXML private Pane paneFlag;
	@FXML private Label labelID;
	@FXML private HBox boxIcons;
	@FXML private Label labelDesc;
	@FXML private VBox boxAttribs;




	public TaskCard(Task task) {
		this.task = task;
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

		final int id 			= (Integer) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID));
		final TaskFlag flag 	= (TaskFlag) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG));
		final String descr 		= (String) TaskLogic.getValue(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION));

		labelID.setText("T-" + id);
		labelDesc.setText(descr);
		paneFlag.setStyle("-fx-background-color: " + flag.color.get().asHex());
	}




	public Task getTask() {
		return this.task;
	}




	public void dispose() {
	}


}
