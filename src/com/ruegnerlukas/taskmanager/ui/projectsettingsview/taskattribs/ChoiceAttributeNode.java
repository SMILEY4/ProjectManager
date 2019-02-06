package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXEvents;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChoiceAttributeNode extends AnchorPane implements AttributeDataNode {


	private TaskAttribute attribute;

	@FXML private TextField values;
	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;




	public ChoiceAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {

		// create root
		AnchorPane root = (AnchorPane) FXMLUtils.loadFXML(getClass().getResource("taskattribute_choice.fxml"), this);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());


		// get data
		ChoiceAttributeData attributeData = (ChoiceAttributeData) attribute.data;


		// values
		values.setText(String.join(",", attributeData.values));
		values.focusedProperty().addListener((observable, oldValue, newValue) -> {
			Set<String> valuesSet = new HashSet<>();
			for (String value : values.getText().split(",")) {
				valuesSet.add(value.trim());
			}
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.CHOICE_ATT_VALUES, new TextArrayValue(valuesSet));
		});
		values.setOnAction(event -> {
			Set<String> valuesSet = new HashSet<>();
			for (String value : values.getText().split(",")) {
				valuesSet.add(value.trim());
			}
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.CHOICE_ATT_VALUES, new TextArrayValue(valuesSet));
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		});


		// default value
		defaultValue.getItems().addAll(attributeData.values);
		defaultValue.getSelectionModel().select(attributeData.defaultValue);
		defaultValue.getSelectionModel().selectedItemProperty().addListener(FXEvents.register(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(newValue));

			}
		}, defaultValue.getSelectionModel().selectedItemProperty()));
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
		ChoiceAttributeData attributeData = (ChoiceAttributeData) attribute.data;

		FXEvents.mute(defaultValue.getSelectionModel().selectedItemProperty());

		values.setText(String.join(",", attributeData.values));
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.getItems().clear();
		defaultValue.getItems().addAll(attributeData.values);
		defaultValue.getSelectionModel().select(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());

		FXEvents.unmute(defaultValue.getSelectionModel().selectedItemProperty());

	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
