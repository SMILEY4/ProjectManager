package com.ruegnerlukas.taskmanager_old.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberPairValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values.FilterValue;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.List;

public class NumberFilterValue extends FilterValue {


	private TaskAttributeValue value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {

		NumberAttributeData numberData = (NumberAttributeData) data;
		int decPlaces = numberData.decPlaces;
		double min = numberData.min;
		double max = numberData.max;

		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			if (numberData.decPlaces > 0) {
				value = new NumberValue(0.0);
				if (compValue instanceof NumberValue) {
					value = compValue;
				}
				Spinner<Double> spinner = buildDoubleSpinner(min, max, 1.0 / Math.pow(10, decPlaces), decPlaces, ((NumberValue) value).getDouble());
				outNodes.add(spinner);
				spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberValue(spinner.getValue());
					onAction();
				});

			} else {
				value = new NumberValue(0);
				if (compValue instanceof NumberValue) {
					value = compValue;
				}
				Spinner<Integer> spinner = buildIntSpinner((int) min, (int) max, 1, ((NumberValue) value).getInt());
				outNodes.add(spinner);
				spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberValue(spinner.getValue());
					onAction();
				});
			}


		}


		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new TextArrayValue();
			if (compValue instanceof TextArrayValue) {
				value = compValue;
			}

			TextField textField = buildTextField("Comma Separated values", String.join(",", ((TextArrayValue) value).getText()));
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


			if (numberData.decPlaces > 0) {
				value = new NumberValue(0.0);
				if (compValue instanceof NumberValue) {
					value = compValue;
				}
				Spinner<Double> spinner = buildDoubleSpinner(min, max, 1.0 / Math.pow(10, decPlaces), decPlaces, ((NumberValue) value).getDouble());
				outNodes.add(spinner);
				spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberValue(spinner.getValue());
					onAction();
				});

			} else {
				value = new NumberValue(0);
				if (compValue instanceof NumberValue) {
					value = compValue;
				}
				Spinner<Integer> spinner = buildIntSpinner((int) min, (int) max, 1, ((NumberValue) value).getInt());
				outNodes.add(spinner);
				spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberValue(spinner.getValue());
					onAction();
				});
			}

		}


		if (FilterCriteria.ComparisonOp.IN_RANGE == compOp || FilterCriteria.ComparisonOp.NOT_IN_RANGE == compOp) {

			if (numberData.decPlaces > 0) {
				value = new NumberPairValue(0.0, 999.0);
				if (compValue instanceof NumberPairValue) {
					value = compValue;
				}

				Spinner<Double> spinnerMin = buildDoubleSpinner(min, max, 1.0 / Math.pow(10, decPlaces), decPlaces, (((NumberPairValue) value).getDouble0()));
				outNodes.add(spinnerMin);
				spinnerMin.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberPairValue(spinnerMin.getValue(), ((NumberPairValue) value).getDouble1());
					onAction();
				});

				Spinner<Double> spinnerMax = buildDoubleSpinner(min, max, 1.0 / Math.pow(10, decPlaces), decPlaces, (((NumberPairValue) value).getDouble1()));
				outNodes.add(spinnerMax);
				spinnerMax.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberPairValue(((NumberPairValue) value).getDouble0(), spinnerMax.getValue());
					onAction();
				});

			} else {
				value = new NumberPairValue(0, 999);
				if (compValue instanceof NumberPairValue) {
					value = compValue;
				}

				Spinner<Integer> spinnerMin = buildIntSpinner((int) min, (int) max, 1, ((NumberPairValue) value).getInt0());
				outNodes.add(spinnerMin);
				spinnerMin.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberPairValue(spinnerMin.getValue(), ((NumberPairValue) value).getInt1());
					onAction();
				});

				Spinner<Integer> spinnerMax = buildIntSpinner((int) min, (int) max, 1, ((NumberPairValue) value).getInt1());
				outNodes.add(spinnerMax);
				spinnerMax.valueProperty().addListener((observable, oldValue, newValue) -> {
					value = new NumberPairValue(((NumberPairValue) value).getInt0(), spinnerMax.getValue());
					onAction();
				});
			}

		}


	}




	@Override
	public TaskAttributeValue getValue() {
		return this.value;
	}


}
