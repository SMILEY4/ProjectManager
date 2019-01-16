package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.List;

public class IDFilterValue extends FilterValue {

	private Object value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, Object compValue) {


		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = 0;
			if(compValue instanceof Integer) {
				value = compValue;
			}

			Spinner<Integer> spinner = buildIntSpinner(0, Integer.MAX_VALUE, 1, (Integer)value);
			outNodes.add(spinner);

			spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = spinner.getValue();
				onAction();
			});

		}



		if (FilterCriteria.ComparisonOp.IN_LIST == compOp || FilterCriteria.ComparisonOp.NOT_IN_LIST == compOp) {

			value = new String[0];
			if(compValue instanceof String[]) {
				value = compValue;
			}

			String[] startValues = new String[((String[])value).length];
			for (int i = 0; i < startValues.length; i++) {
				startValues[i] = ((String[])value)[i];
			}

			TextField textField = buildTextField("Comma Separated values", String.join(",", startValues));
			outNodes.add(textField);

			textField.setOnAction(event -> {
				value = textField.getText().split(",");
				onAction();
			});

			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				value = textField.getText().split(",");
				onAction();
			});
		}

		if (FilterCriteria.ComparisonOp.GREATER_THAN == compOp
				|| FilterCriteria.ComparisonOp.GREATER_THAN_EQUAL == compOp
				|| FilterCriteria.ComparisonOp.LESS_THAN == compOp
				|| FilterCriteria.ComparisonOp.LESS_THAN_EQUAL == compOp) {


			value = 0;
			if(compValue instanceof Integer) {
				value = compValue;
			}

			Spinner<Integer> spinner = buildIntSpinner(0, Integer.MAX_VALUE, 1, (Integer)value);
			outNodes.add(spinner);
			spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = spinner.getValue();
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.IN_RANGE == compOp || FilterCriteria.ComparisonOp.NOT_IN_RANGE == compOp) {

			value = new Integer[]{0,999};
			if(compValue instanceof Integer[] && ((Integer[])compValue).length == 2) {
				value = compValue;
			}

			Spinner<Integer> spinnerMin = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((Integer[])value)[0] );
			outNodes.add(spinnerMin);
			spinnerMin.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new Integer[]{ spinnerMin.getValue(), ((Integer[])value)[1]};
				onAction();
			});

			Spinner<Integer> spinnerMax = buildIntSpinner(0, Integer.MAX_VALUE, 1, ((Integer[])value)[1] );
			outNodes.add(spinnerMax);
			spinnerMax.valueProperty().addListener((observable, oldValue, newValue) -> {
				value = new Integer[]{ ((Integer[])value)[0], spinnerMax.getValue()};
				onAction();
			});
		}


	}




	@Override
	public Object getValue() {
		return this.value;
	}


}
