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




	@Override
	public String toString() {
		return Boolean.toString(value);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BoolValue boolValue = (BoolValue) o;
		return value == boolValue.value;
	}




	@Override
	public int hashCode() {
		return (value ? 1 : 0);
	}

}
