package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.updaters.AttributeUpdater;
import com.ruegnerlukas.taskmanager.logic.attributes.validation.AttributeValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeWarningLogic {


	private boolean requiresWarningAttribute(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> newValues) {

		// is there even a difference between old and new values of given attribute ?
		boolean isDifferent = false;
		for (TaskAttributeData.Var var : newValues.keySet()) {
			TaskAttributeValue valueNew = newValues.get(var);
			TaskAttributeValue valueTask = attribute.data.getValue(var);
			if (!valueNew.equals(valueTask)) {
				isDifferent = true;
				break;
			}
		}

		return isDifferent;
	}




	private boolean requiresWarningTasks(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> newValues) {

		// are tasks valid with new values ?
		TaskAttributeData dataCopy = attribute.data.copy();
		AttributeValidator validator = Logic.attribute.getAttributeValidator(attribute.data.getType());
		AttributeUpdater updater = Logic.attribute.getAttributeUpdater(attribute.data.getType());

		Map<TaskAttributeData.Var, TaskAttributeValue> changedValues = new HashMap<>(newValues);
		for (Map.Entry<TaskAttributeData.Var, TaskAttributeValue> entry : newValues.entrySet()) {
			updater.update(dataCopy, entry.getKey(), entry.getValue(), changedValues);
		}

		List<Task> affectedTasks = new ArrayList<>();
		for (int i = 0, n = Logic.tasks.getTasksInternal().size(); i < n; i++) {
			Task task = Logic.tasks.getTasksInternal().get(i);
			TaskAttributeValue valueTask = Logic.tasks.getValue(task, attribute);
			if (!validator.validate(dataCopy, valueTask)) {
				affectedTasks.add(task);
			}
		}

		return !affectedTasks.isEmpty();
	}




	private boolean requiresWarningInternal(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> newValues) {
		if (!requiresWarningAttribute(attribute, newValues)) {
			return false;
		} else {
			return requiresWarningTasks(attribute, newValues);
		}
	}




	public Response<Boolean> requiresWarning(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> newValues) {
		return new Response<Boolean>().complete(requiresWarningInternal(attribute, newValues));
	}


}
