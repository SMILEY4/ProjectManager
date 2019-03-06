package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs_old;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;

public class NumberAttributeNode extends AttributeDataNode {


	@FXML private Spinner<Integer> decPlaces;
	@FXML private Spinner<Double> minValue;
	@FXML private Spinner<Double> maxValue;
	@FXML private CheckBox useDefault;
	@FXML private Spinner<Double> defaultValue;




	public NumberAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "fxmlfiles/taskattribute_number.fxml", true);
	}




	@Override
	protected void onCreate() {

		// get data
		NumberAttributeData attributeData = (NumberAttributeData) getAttribute().data;

		// dec places
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, (observable, oldValue, newValue) -> {
			refreshSpinners();
			setChanged();
		});

		// min value
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			refreshSpinners();
			setChanged();
		});

		// max value
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			refreshSpinners();
			setChanged();
		});

		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			defaultValue.setDisable(!useDefault.isSelected());
			setChanged();
		});

		// default value
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, (observable, oldValue, newValue) -> {
			setChanged();
		});
		defaultValue.setDisable(!useDefault.isSelected());

	}




	private void refreshSpinners() {
		SpinnerUtils.initSpinner(minValue, minValue.getValue(), Integer.MIN_VALUE, maxValue.getValue(), Math.pow(10, -decPlaces.getValue()), decPlaces.getValue(), true, null);
		SpinnerUtils.initSpinner(maxValue, maxValue.getValue(), minValue.getValue(), Integer.MAX_VALUE, Math.pow(10, -decPlaces.getValue()), decPlaces.getValue(), true, null);
		SpinnerUtils.initSpinner(defaultValue, defaultValue.getValue(), minValue.getValue(), maxValue.getValue(), Math.pow(10, -decPlaces.getValue()), decPlaces.getValue(), true, null);
	}




	@Override
	protected void onChange() {
		NumberAttributeData attributeData = (NumberAttributeData) getAttribute().data;
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, null);
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		useDefault.setSelected(attributeData.useDefault);
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, true, null);
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	protected void onSave() {
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES, new NumberValue(decPlaces.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.NUMBER_ATT_MIN, new NumberValue(minValue.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.NUMBER_ATT_MAX, new NumberValue(maxValue.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.DEFAULT_VALUE, new NumberValue(defaultValue.getValue()));
	}




	@Override
	protected void onDiscard() {
		onChange();
	}




	@Override
	protected void onClose() {
	}




	@Override
	public void close() {
		EventManager.deregisterListeners(this);
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}




	@Override
	public boolean getUseDefault() {
		return useDefault.isSelected();
	}

}
