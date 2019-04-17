package com.ruegnerlukas.taskmanager_old.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values.FilterValue;
import com.ruegnerlukas.taskmanager.utils.uielements.choicelistfield.ChoiceListField;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlagFilterValue extends FilterValue {


	private TaskAttributeValue value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {

		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = new FlagValue(((FlagAttributeData) data).defaultFlag);
			if (compValue instanceof FlagValue) {
				value = compValue;
			}

			ComboBox<TaskFlag> choice = buildComboxFlag(((FlagValue) value).getFlag(), ((FlagAttributeData) data).flags);
			outNodes.add(choice);

			choice.setOnAction(event -> {
				value = new FlagValue(choice.getSelectionModel().getSelectedItem());
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new FlagArrayValue();
			if (compValue instanceof FlagArrayValue) {
				value = compValue;
			}

			String[] startValues = new String[((FlagArrayValue) value).getFlags().length];
			for (int i = 0; i < startValues.length; i++) {
				startValues[i] = ((FlagArrayValue) value).getFlags()[i].name;
			}

			FlagAttributeData flagData = (FlagAttributeData) data;
			Set<String> entries = new HashSet<>();
			for (TaskFlag flag : flagData.flags) {
				entries.add(flag.name);
			}

			ChoiceListField choiceField = buildChoiceList("Comma Separated values", startValues, entries);
			outNodes.add(choiceField);

			choiceField.setOnAction(event -> {
				String[] values = choiceField.getChoicesArray();
				TaskFlag[] flags = new TaskFlag[values.length];
				for (int i = 0; i < values.length; i++) {
					flags[i] = TaskFlag.findFlag(values[i].trim(), ((FlagAttributeData) data).flags);
				}
				value = new FlagArrayValue(flags);
				onAction();
			});

			choiceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = choiceField.getChoicesArray();
				TaskFlag[] flags = new TaskFlag[values.length];
				for (int i = 0; i < values.length; i++) {
					flags[i] = TaskFlag.findFlag(values[i].trim(), ((FlagAttributeData) data).flags);
				}
				value = new FlagArrayValue(flags);
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.CONTAINS == compOp || FilterCriteria.ComparisonOp.CONTAINS_NOT == compOp) {

			value = new TextValue("");
			if (compValue instanceof TextValue) {
				value = compValue;
			}

			TextField textField = buildTextField("", ((TextValue) value).getText());
			outNodes.add(textField);

			textField.setOnAction(event -> {
				value = new TextValue(textField.getText());
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = new TextValue(textField.getText());
			});

		}

	}




	@Override
	public TaskAttributeValue getValue() {
		return this.value;
	}


}