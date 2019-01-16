package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class BoolFilterValue extends FilterValue {

	private boolean value = true;


	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, Object compValue) {

		if (FilterCriteria.ComparisonOp.EQUALITY == compOp || FilterCriteria.ComparisonOp.INEQUALITY == compOp) {
			if(compValue instanceof Boolean) {
				value = (Boolean)compValue;
			}
			ChoiceBox<String> choice = buildChoiceBox( (value ? "True" : "False"), "True", "False");
			outNodes.add(choice);
			choice.setOnAction(event -> {
				value = choice.getSelectionModel().getSelectedItem().equals("True");
				this.onAction();
			});
		}

	}




	@Override
	public Object getValue() {
		return this.value;
	}


}
