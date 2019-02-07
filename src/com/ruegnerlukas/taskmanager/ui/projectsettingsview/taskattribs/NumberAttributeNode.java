package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NumberAttributeNode extends AnchorPane implements AttributeDataNode {


	private TaskAttribute attribute;

	@FXML private Spinner<Integer> decPlaces;
	@FXML private Spinner<Double> minValue;
	@FXML private Spinner<Double> maxValue;
	@FXML private CheckBox useDefault;
	@FXML private Spinner<Double> defaultValue;




	public NumberAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {

		// create root
		AnchorPane root = (AnchorPane) FXMLUtils.loadFXML(getClass().getResource("taskattribute_number.fxml"), this);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());


		// get data
		NumberAttributeData attributeData = (NumberAttributeData) attribute.data;


		// dec places
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES, new NumberValue(decPlaces.getValue()));
		});


		// min value
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_MIN, new NumberValue(minValue.getValue()));
		});


		// max value
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_MAX, new NumberValue(maxValue.getValue()));
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		});


		// default value
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new NumberValue(defaultValue.getValue()));
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
		NumberAttributeData attributeData = (NumberAttributeData) attribute.data;
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, null);
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		useDefault.setSelected(attributeData.useDefault);
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
