package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs_old;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

public class BoolAttributeNode extends AttributeDataNode {


	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;




	public BoolAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "fxmlfiles/taskattribute_bool.fxml", true);
	}




	@Override
	protected void onCreate() {

		// get data
		BoolAttributeData attributeData = (BoolAttributeData) getAttribute().data;

		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			setChanged();
		});

		// default value
		defaultValue.getItems().addAll("True", "False");
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			setChanged();
		});
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	protected void onChange() {
		BoolAttributeData attributeData = (BoolAttributeData) getAttribute().data;
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	protected void onSave() {
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.DEFAULT_VALUE, new BoolValue(defaultValue.getValue().equalsIgnoreCase("True")));
	}




	@Override
	protected void onDiscard() {
		onChange();
	}




	@Override
	public void onClose() {
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
