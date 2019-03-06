package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs_old;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXEvents;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Set;

public class ChoiceAttributeNode extends AttributeDataNode {


	@FXML private TextField values;
	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;




	public ChoiceAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "fxmlfiles/taskattribute_choice.fxml", true);
	}




	@Override
	protected void onCreate() {

		// get data
		ChoiceAttributeData attributeData = (ChoiceAttributeData) getAttribute().data;

		// values
		values.setText(String.join(",", attributeData.values));
		values.focusedProperty().addListener((observable, oldValue, newValue) -> {
			setChanged();
		});
		values.setOnAction(event -> {
			setChanged();
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			setChanged();
		});


		// default value
		defaultValue.getItems().addAll(attributeData.values);
		defaultValue.getSelectionModel().select(attributeData.defaultValue);
		defaultValue.getSelectionModel().selectedItemProperty().addListener(FXEvents.register((observable, oldValue, newValue) -> {
			setChanged();
		}, defaultValue.getSelectionModel().selectedItemProperty()));
		defaultValue.setDisable(!useDefault.isSelected());

	}




	private Set<String> getValues() {
		Set<String> set = new HashSet<>();
		for (String value : values.getText().split(",")) {
			set.add(value.trim());
		}
		return set;
	}




	@Override
	protected void onChange() {
		ChoiceAttributeData attributeData = (ChoiceAttributeData) getAttribute().data;

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
	protected void onSave() {
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.CHOICE_ATT_VALUES, new TextArrayValue(getValues()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getValue()));
	}




	@Override
	protected void onDiscard() {
		onChange();
	}




	@Override
	protected void onClose() {
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
