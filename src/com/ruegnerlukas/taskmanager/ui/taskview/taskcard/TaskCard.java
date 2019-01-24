package com.ruegnerlukas.taskmanager.ui.taskview.taskcard;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.TaskValueChangedEvent;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabelarea.EditableAreaLabel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class TaskCard extends AnchorPane {


	public Task task;

	@FXML private Pane paneFlag;
	@FXML private Pane paneBackground;
	@FXML private Label labelID;
	@FXML private AnchorPane paneDescription;

	private EditableAreaLabel areaDescription;




	public TaskCard(Task task) {
		this.task = task;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_taskcard.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskCard-FXML: " + e);
		}

		this.setPrefSize(10000, 200);

		create();
	}




	private void create() {

		// id
		int id = ((NumberValue)Logic.tasks.getAttributeValue(task, IDAttributeData.NAME)).getInt();
		labelID.setText("T-" + id);


		// flag
		Color flagColor = ((FlagValue)Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME)).getFlag().color.color;
		paneFlag.setStyle("-fx-background-color: rgba(" + flagColor.getRed()*255 +","+ flagColor.getGreen()*255 +","+ flagColor.getBlue()*255 + ",1.0);");

		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent)e;
			TaskAttribute attribute = event.getAttribute();
			if(attribute.data.getType() == TaskAttributeType.FLAG) {
				if(event.wasChanged(TaskAttributeData.Var.DEFAULT_VALUE) || event.wasChanged(TaskAttributeData.Var.FLAG_ATT_FLAGS)) {
					Color newColor = ((FlagValue)Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME)).getFlag().color.color;
					paneFlag.setStyle("-fx-background-color: rgba(" + newColor.getRed()*255 +","+ newColor.getGreen()*255 +","+ newColor.getBlue()*255 + ",1.0);");
				}
			}
		}, AttributeUpdatedEvent.class);


		// description
		areaDescription = new EditableAreaLabel();
		areaDescription.setText( ((TextValue)(Logic.tasks.getAttributeValue(task, DescriptionAttributeData.NAME))).getText() );
		AnchorUtils.setAnchors(areaDescription, 0, 0, 0, 0);
		paneDescription.getChildren().add(areaDescription);

		areaDescription.addListener((observable, oldValue, newValue) -> {
			Logic.tasks.setAttributeValue(task, Logic.attribute.getAttributes(TaskAttributeType.DESCRIPTION).get(0), new TextValue(newValue));
		});

		EventManager.registerListener(this, e -> {
			TaskValueChangedEvent event = (TaskValueChangedEvent)e;
			if(event.getTask() == task) {

				TaskAttribute attribute = event.getAttribute();
				if(attribute.data.getType() == TaskAttributeType.DESCRIPTION) {
					TextValue newDescription = (TextValue)event.getNewValue();
					areaDescription.setText(newDescription.getText());
				}

			}
		}, TaskValueChangedEvent.class);

	}




}
