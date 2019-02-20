package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;

public class BoolAttributeData implements TaskAttributeData {


	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
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
