package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BoolAttributeNode extends AnchorPane implements AttributeRequirementNode {


	private TaskAttribute attribute;

	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;




	public BoolAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			create();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void create() throws IOException {

		// create root
		AnchorPane root = (AnchorPane) FXMLUtils.loadFXML(getClass().getResource("taskattribute_bool.fxml"), this);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());


		// get data
		BoolAttributeData attributeData = (BoolAttributeData) attribute.data;


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		});


		// default value
		defaultValue.getItems().addAll("True", "False");
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new BoolValue(newValue.equalsIgnoreCase("True")));
		});
		defaultValue.setDisable(!useDefault.isSelected());


		// listen for changes / rejections
		EventManager.registerListener(this, e -> {
			TaskAttribute eventAttribute = null;
			if(e instanceof  AttributeUpdatedEvent) {
				eventAttribute = ((AttributeUpdatedEvent) e).getAttribute();
			} else {
				eventAttribute = ((AttributeUpdatedRejection) e).getAttribute();
			}
			if (eventAttribute == attribute) {
				updateData();
			}
		}, AttributeUpdatedEvent.class, AttributeUpdatedRejection.class);

	}




	@Override
	public void close() {
		EventManager.deregisterListeners(this);
	}




	private void updateData() {
		BoolAttributeData attributeData = (BoolAttributeData) attribute.data;
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
