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
import javafx.scene.control.ComboBox;

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

			ChoiceListField choiceField = buildChoiceList("Comma Separated IDs", getValueAsIDString((TaskArrayValue) value), getAllIDsAsChoices());
			outNodes.add(choiceField);

			choiceField.setOnAction(event -> {
				String[] values = choiceField.getChoicesArray();
				this.value = new TaskArrayValue(toTaskSet(values));
				onAction();
			});
			choiceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				String[] values = choiceField.getChoicesArray();
				this.value = new TaskArrayValue(toTaskSet(values));
				onAction();
			});
		}


		if (FilterCriteria.ComparisonOp.IS_INDEPENDENT == compOp) {

			value = new BoolValue(true);
			if (compValue instanceof BoolValue) {
				value = compValue;
			}

			ComboBox<Boolean> choice = buildComboxBoolean( ((BoolValue)value).getBoolValue() );
			outNodes.add(choice);

			choice.setOnAction(event -> {
				value = new BoolValue(choice.getSelectionModel().getSelectedItem());
				this.onAction();
			});
		}

	}




	private String[] getValueAsIDString(TaskArrayValue value) {
		if (value == null) {
			return new String[]{};
		}
		String[] strings = new String[value.getTasks().length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = Integer.toString(value.getTasks()[i].getID());
		}
		return strings;
	}




	private Set<String> getAllIDsAsChoices() {
		Set<String> set = new HashSet<>();
		SyncRequest<List<Task>> request = new SyncRequest<>();
		Logic.tasks.getTasks(request);
		for (Task task : request.getResponse().getValue()) {
			set.add(Integer.toString(task.getID()));
		}
		return set;
	}




	private Set<Task> toTaskSet(String[] strings) {
		Set<Task> set = new HashSet<>();
		for (String str : strings) {
			SyncRequest<Task> request = new SyncRequest<>();
			Logic.tasks.getTaskByID(Integer.parseInt(str), request);
			Response<Task> response = request.getResponse();
			if (response.getState() == Response.State.SUCCESS) {
				set.add(response.getValue());
			}
		}
		return set;
	}




	@Override
	public TaskAttributeValue getValue() {
		return this.value;
	}


}
