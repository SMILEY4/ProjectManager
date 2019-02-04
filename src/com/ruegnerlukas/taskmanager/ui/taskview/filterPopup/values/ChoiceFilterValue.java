package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.utils.uielements.choicelistfield.ChoiceListField;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ChoiceFilterValue extends FilterValue {


	private TaskAttributeValue value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {


		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = new TextValue(((ChoiceAttributeData) data).defaultValue);
			if (compValue instanceof TextValue) {
				value = compValue;
			}

			ChoiceBox<String> choice = buildChoiceBox(((TextValue) value).getText(), ((ChoiceAttributeData) data).values);
			outNodes.add(choice);
			choice.setOnAction(event -> {
				value = new TextValue(choice.getSelectionModel().getSelectedItem());
				onAction();
			});

		}


		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new TextArrayValue();
			if (compValue instanceof TextArrayValue) {
				value = compValue;
			}

			ChoiceAttributeData choiceData = (ChoiceAttributeData) data;
			ChoiceListField choiceField = buildChoiceList("Comma Separated values", ((TextArrayValue) value).getText(), new HashSet<>(Arrays.asList(choiceData.values)));
			outNodes.add(choiceField);

			choiceField.setOnAction(event -> {
				String[] values = choiceField.getChoicesArray();
				this.value = new TextArrayValue(values);
				onAction();
			});
			choiceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = choiceField.getChoicesArray();
				this.value = new TextArrayValue(values);
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.CONTAINS == compOp || FilterCriteria.ComparisonOp.CONTAINS_NOT == compOp) {

			value = new TextValue("");
			if (compValue instanceof TextValue) {
				value = compValue;
			}

			TextField textField = buildTextField("", ((TextValue) value).getText());
			textField.setOnAction(event -> {
				value = new TextValue(textField.getText());
				onAction();
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = new TextValue(textField.getText());
				onAction();
			});
		}

	}




	@Override
	public TaskAttributeValue getValue() {
		return this.value;
	}


}
