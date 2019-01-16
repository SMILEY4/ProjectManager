package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class ChoiceFilterValue extends FilterValue {

	private Object value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, Object compValue) {


		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = ((ChoiceAttributeData)data).defaultValue;
			if(compValue instanceof String) {
				value = compValue;
			}

			ChoiceBox<String> choice = buildChoiceBox((String)value, ((ChoiceAttributeData)data).values);
			outNodes.add(choice);
			choice.setOnAction(event -> {
				value = choice.getSelectionModel().getSelectedItem();
				onAction();
			});

		}



		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new String[]{""};
			if(compValue instanceof String[]) {
				value = compValue;
			}

			TextField textField = buildTextField("Comma Separated values", String.join(",", ((String[])value)));
			outNodes.add(textField);

			textField.setOnAction(event -> {
				String[] values = textField.getText().split(",");
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
				this.value = values;
				onAction();
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = textField.getText().split(",");
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
				this.value = values;
				onAction();
			});
		}



		if (FilterCriteria.ComparisonOp.CONTAINS == compOp || FilterCriteria.ComparisonOp.CONTAINS_NOT == compOp) {

			value = "";
			if(compValue instanceof String) {
				value = compValue;
			}

			TextField textField = buildTextField("", (String)value);
			textField.setOnAction(event -> {
				value = textField.getText();
				onAction();
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = textField.getText();
				onAction();
			});
		}

	}




	@Override
	public Object getValue() {
		return this.value;
	}


}
