package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;

public class NumberAttributeData implements TaskAttributeData {


	public int decPlaces = 0;
	public double min = -100;
	public double max = +100;
	public boolean useDefault = false;
	public double defaultValue = 0;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.NUMBER;
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public NumberValue getDefault() {
		return new NumberValue(defaultValue);
	}

}
