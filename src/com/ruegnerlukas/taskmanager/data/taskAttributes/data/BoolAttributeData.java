package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class BoolAttributeData implements TaskAttributeData {


	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
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
