package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberPairValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.List;

public class IDFilterValue extends FilterValue {


	private TaskAttributeValue value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {


		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = new NumberValue(0);
			if (compValue instanceof NumberValue) {
				value = compValue;
			}

			Spinner<Integer> spinner = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((NumberValue)value).getInt());
			outNodes.add(spinner);

			spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new NumberValue(spinner.getValue());
				onAction();
			});

		}


		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new TextArrayValue();
			if (compValue instanceof TextArrayValue) {
				value = compValue;
			}

			TextField textField = buildTextField("Comma Separated values", String.join(",", ((TextArrayValue)value).getText() ));
			outNodes.add(textField);

			textField.setOnAction(event -> {
				value = new TextArrayValue(textField.getText().split(","));
				onAction();
			});

			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = new TextArrayValue(textField.getText().split(","));
				onAction();
			});
		}

		if (FilterCriteria.ComparisonOp.GREATER_THAN == compOp
				|| FilterCriteria.ComparisonOp.GREATER_THAN_EQUAL == compOp
				|| FilterCriteria.ComparisonOp.LESS_THAN == compOp
				|| FilterCriteria.ComparisonOp.LESS_THAN_EQUAL == compOp) {


			value = new NumberValue(0);
			if (compValue instanceof NumberValue) {
				value = compValue;
			}

			Spinner<Integer> spinner = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((NumberValue) value).getInt());
			outNodes.add(spinner);
			spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new NumberValue(spinner.getValue());
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.IN_RANGE == compOp || FilterCriteria.ComparisonOp.NOT_IN_RANGE == compOp) {

			value = new NumberPairValue(0, 999);
			if (compValue instanceof NumberPairValue) {
				value = compValue;
			}

			Spinner<Integer> spinnerMin = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((NumberPairValue)value).getInt0());
			outNodes.add(spinnerMin);
			spinnerMin.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new NumberPairValue(spinnerMin.getValue(), ((NumberPairValue)value).getInt1());
				onAction();
			});

			Spinner<Integer> spinnerMax = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((NumberPairValue)value).getInt1());
			outNodes.add(spinnerMax);
			spinnerMax.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new NumberPairValue(((NumberPairValue)value).getInt0(), spinnerMin.getValue());
				onAction();
			});
		}


	}




	@Override
	public TaskAttributeValue getValue() {
		return this.value;
	}


}
