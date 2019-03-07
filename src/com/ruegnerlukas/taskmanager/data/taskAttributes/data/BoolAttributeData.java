package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

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




	@Override
	public TaskAttributeValue getValue(Var var) {
		if (var == Var.USE_DEFAULT) {
			return new BoolValue(useDefault);
		}
		if (var == Var.DEFAULT_VALUE) {
			return getDefault();
		}
		return null;
	}




	@Override
	public BoolAttributeData copy() {
		BoolAttributeData copy = new BoolAttributeData();
		copy.useDefault = this.useDefault;
		copy.defaultValue = this.defaultValue;
		return copy;
	}


}
