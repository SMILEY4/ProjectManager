package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.HashMap;
import java.util.Map;

public class DependencyAttributeData implements TaskAttributeData {


	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DEPENDENCY;
	}




	@Override
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		return new HashMap<>();
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !usesDefault();
		} else {
			return value instanceof TaskArrayValue;
		}
	}




	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public TaskArrayValue getDefault() {
		return new TaskArrayValue();
	}

}
