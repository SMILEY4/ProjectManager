package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

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




	@Override
	public TaskAttributeValue getValue(Var var) {
		return null;
	}




	@Override
	public IDAttributeData copy() {
		IDAttributeData copy = new IDAttributeData();
		return copy;
	}

}
