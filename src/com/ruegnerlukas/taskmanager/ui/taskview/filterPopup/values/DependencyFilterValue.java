package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.uielements.choicelistfield.ChoiceListField;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DependencyFilterValue extends FilterValue {


	private TaskAttributeValue value = null;




	@Override
	public void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue) {

		if (FilterCriteria.ComparisonOp.IS_DEPENDENT_ON_FILTER == compOp || FilterCriteria.ComparisonOp.IS_PREREQUISITE_OF_FILTER == compOp) {

			value = new TaskArrayValue();
			if (compValue instanceof TaskArrayValue) {
				value = compValue;
			}

			ChoiceListField choiceField = buildChoiceList("Comma Separated IDs", asIDString(((TaskArrayValue) value).getTasks()), getIDChoices());
			outNodes.add(choiceField);

			choiceField.setOnAction(event -> {
				String[] values = choiceField.getChoicesArray();
				this.value = toTaskArray(values);
				onAction();
			});
			choiceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = choiceField.getChoicesArray();
				this.value = toTaskArray(values);
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.IS_INDEPENDENT == compOp) {

			value = new BoolValue(true);
			if (compValue instanceof BoolValue) {
				value = compValue;
			}

			ChoiceBox<String> choice = buildChoiceBox(((BoolValue) value).getBoolValue() ? "True" : "False", "True", "False");
			outNodes.add(choice);
			choice.setOnAction(event -> {
				value = new BoolValue(choice.getSelectionModel().getSelectedItem().equalsIgnoreCase("True"));
				onAction();
			});

		}

	}




	private String[] asIDString(Task... tasks) {
		String[] values = new String[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			values[i] = Integer.toString(tasks[i].getID());
		}
		return values;
	}




	private Set<String> getIDChoices() {
		Set<String> set = new HashSet<>();
		SyncRequest<List<Task>> request = new SyncRequest<>();
		Logic.tasks.getTasks(request);
		for (Task task : request.getResponse().getValue()) {
			set.add(Integer.toString(task.getID()));
		}
		return set;
	}




	private TaskArrayValue toTaskArray(String[] values) {
		List<Task> list = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			String value = values[i].trim();
			boolean isNumber = true;
			for (char c : value.toCharArray()) {
				if (!Character.isDigit(c)) {
					isNumber = false;
					break;
				}
			}
			if (isNumber && !value.isEmpty()) {
				int id = Integer.parseInt(value);
				SyncRequest<Task> request = new SyncRequest<>();
				Logic.tasks.getTaskByID(id, request);
				Response<Task> response = request.getResponse();
				if (response.getState() == Response.State.SUCCESS) {
					list.add(response.getValue());
				}
			}
		}

		return new TaskArrayValue(list);
	}




	@Override
	public TaskAttributeValue getValue() {
		return value;
	}


}
