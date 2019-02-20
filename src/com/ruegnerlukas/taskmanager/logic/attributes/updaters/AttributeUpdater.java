package com.ruegnerlukas.taskmanager.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.HashMap;
import java.util.Map;

public abstract class AttributeUpdater {


	public Map<TaskAttributeData.Var, TaskAttributeValue> update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue) {
		Map<TaskAttributeData.Var, TaskAttributeValue> changedVars = new HashMap<>();
		if (!update(data, var, newValue, changedVars)) {
			changedVars.clear();
		}
		return changedVars;
	}




	public abstract boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars);

}
