package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.HashMap;
import java.util.Map;

public class BoolAttributeData implements TaskAttributeData {


	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
	}




	@Override
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		Map<Var, TaskAttributeValue> changedVars = new HashMap<>();

		switch (var) {

			case USE_DEFAULT: {
				if (newValue instanceof BoolValue) {
					useDefault = ((BoolValue) newValue).getBoolValue();
					changedVars.put(Var.USE_DEFAULT, newValue);
				}
				break;
			}

			case DEFAULT_VALUE: {
				if (newValue instanceof BoolValue) {
					defaultValue = ((BoolValue) newValue).getBoolValue();
					changedVars.put(Var.DEFAULT_VALUE, newValue);
				}
				break;
			}
		}

		return changedVars;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !useDefault;
		} else {
			return (value instanceof BoolValue);
		}
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public BoolValue getDefault() {
		return new BoolValue(defaultValue);
	}


}
