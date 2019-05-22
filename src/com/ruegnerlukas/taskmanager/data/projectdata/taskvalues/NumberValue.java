package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

public class NumberValue extends TaskValue<Double> {


	public NumberValue(double value) {
		super(value, AttributeType.NUMBER);
	}




	public int getInt() {
		return getValue().intValue();
	}




	public double getDouble() {
		return getValue();
	}




	@Override
	public String asDisplayableString() {
		return Double.toString(getValue());
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return Double.compare(this.getValue(), ((NumberValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Number@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
