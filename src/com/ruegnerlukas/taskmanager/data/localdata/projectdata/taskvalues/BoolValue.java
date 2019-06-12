package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;

public class BoolValue extends TaskValue<Boolean> {


	public BoolValue(boolean value) {
		super(value, AttributeType.BOOLEAN);
	}




	@Override
	public String asDisplayableString() {
		return getValue() ? "True" : "False";
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? 1 : other.getAttType().ordinal());
		} else {
			return Boolean.compare(this.getValue(), ((BoolValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Bool@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}


}
