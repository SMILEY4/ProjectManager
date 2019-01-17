package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

public class BoolValue implements TaskAttributeValue {


	private boolean value;




	public BoolValue(boolean value) {
		this.value = value;
	}




	public boolean getBoolValue() {
		return value;
	}




	@Override
	public Object getValue() {
		return value;
	}

}
