package com.ruegnerlukas.taskmanager_old.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values.FilterValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.List;

public class DescriptionFilterValue extends FilterValue {


	private String value = "";




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {


		if (FilterCriteria.ComparisonOp.EQUALITY == compOp
				|| FilterCriteria.ComparisonOp.INEQUALITY == compOp
				|| FilterCriteria.ComparisonOp.CONTAINS == compOp
				|| FilterCriteria.ComparisonOp.CONTAINS_NOT == compOp) {

			value = "";
			if (compValue instanceof TextValue) {
				value = ((TextValue) compValue).getText();
			}

			TextField textField = buildTextField("", value);
			outNodes.add(textField);
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
	public TaskAttributeValue getValue() {
		return new TextValue(this.value);
	}


}
