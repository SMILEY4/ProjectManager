package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;

public class IDAttributeData implements TaskAttributeData {


	public static final String NAME = "ID Attribute";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.ID;
	}




	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public NumberValue getDefault() {
		return new NumberValue(-1);
	}

}
