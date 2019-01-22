package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
		AnchorPane root = (AnchorPane)FXMLUtils.loadFXML(getClass().getResource("taskattribute_bool.fxml"), this);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());


		// get data
		BoolAttributeData attributeData = (BoolAttributeData) attribute.data;


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
			}
		});


		// default value
		defaultValue.getItems().addAll("True", "False");
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new BoolValue(newValue.equalsIgnoreCase("True")));
			}
		});
		defaultValue.setDisable(!useDefault.isSelected());


		// listen for changes
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
				if (event.getAttribute() == attribute) {
					updateData();
				}
			}
		}, AttributeUpdatedEvent.class);


		// listen for rejections
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeUpdatedRejection event = (AttributeUpdatedRejection) e;
				if (event.getAttribute() == attribute) {
					updateData();
				}
			}
		}, AttributeUpdatedRejection.class);

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
