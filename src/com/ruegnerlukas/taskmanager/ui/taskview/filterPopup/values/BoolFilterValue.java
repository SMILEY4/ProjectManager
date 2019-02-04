package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class BoolFilterValue extends FilterValue {


	private boolean value = true;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {

		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {

			value = true;
			if (compValue instanceof BoolValue) {
				value = ((BoolValue) compValue).getBoolValue();
			}
			ChoiceBox<String> choice = buildChoiceBox((value ? "True" : "False"), "True", "False");
			outNodes.add(choice);

			choice.setOnAction(event -> {
				value = choice.getSelectionModel().getSelectedItem().equals("True");
				this.onAction();
			});
		}

	}




	@Override
	public TaskAttributeValue getValue() {
		return new BoolValue(value);
	}


}
