package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

public class NumberValue implements TaskAttributeValue {


	private double value;
	private boolean isInt;




	public NumberValue(int value) {
		this.value = value;
		this.isInt = true;
	}




	public NumberValue(double value) {
		this.value = value;
		this.isInt = false;
	}




	public int getInt() {
		return (int) value;
	}




	public double getDouble() {
		return value;
	}




	@Override
	public Object getValue() {
		if (isInt) {
			return (int) value;
		} else {
			return value;
		}
	}

}
