package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class FlagFilterValue extends FilterValue {

	private Object value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, Object compValue) {

		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = ((FlagAttributeData)data).defaultFlag;
			if(compValue instanceof TaskFlag) {
				value = compValue;
			}

			String[] flagNames = new String[((FlagAttributeData) data).flags.length];
			for (int i = 0; i < flagNames.length; i++) {
				flagNames[i] = ((FlagAttributeData) data).flags[i].name;
			}

			ChoiceBox<String> choice = buildChoiceBox( ((TaskFlag)value).name, flagNames);
			outNodes.add(choice);

			choice.setOnAction(event -> {
				String name = choice.getSelectionModel().getSelectedItem();
				value = TaskFlag.findFlag(name, ((FlagAttributeData)data).flags);
				onAction();
			});
		}



		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new TaskFlag[0];
			if(compValue instanceof TaskFlag[]) {
				value = compValue;
			}

			String[] startValues = new String[((TaskFlag[])value).length];
			for (int i = 0; i < startValues.length; i++) {
				startValues[i] = ((TaskFlag[])value)[i].name;
			}

			TextField textField = buildTextField("Comma Separated values", String.join(",", startValues));
			outNodes.add(textField);

			textField.setOnAction(event -> {
				String[] values = textField.getText().split(",");
				TaskFlag[] flags = new TaskFlag[values.length];
				for (int i = 0; i < values.length; i++) {
					flags[0] = TaskFlag.findFlag(values[i].trim(), ((FlagAttributeData)data).flags);
				}
				value = flags;
				onAction();
			});

			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = textField.getText().split(",");
				TaskFlag[] flags = new TaskFlag[values.length];
				for (int i = 0; i < values.length; i++) {
					flags[0] = TaskFlag.findFlag(values[i].trim(), ((FlagAttributeData)data).flags);
				}
				value = flags;
				onAction();
			});
		}




		if (FilterCriteria.ComparisonOp.CONTAINS == compOp || FilterCriteria.ComparisonOp.CONTAINS_NOT == compOp) {

			value = "";
			if(compValue instanceof String) {
				value = compValue;
			}

			TextField textField = buildTextField("", ((String)value));
			outNodes.add(textField);

			textField.setOnAction(event -> {
				value = textField.getText();
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = textField.getText();
			});

		}

	}




	@Override
	public Object getValue() {
		return this.value;
	}


}
