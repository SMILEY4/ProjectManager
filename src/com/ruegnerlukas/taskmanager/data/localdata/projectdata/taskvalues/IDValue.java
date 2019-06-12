package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;

public class IDValue extends TaskValue<Integer> {


	public IDValue(int value) {
		super(value, AttributeType.ID);
	}




	@Override
	public String asDisplayableString() {
		return getValue().toString();
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? 1 : other.getAttType().ordinal());
		} else {
			return Integer.compare(this.getValue(), ((IDValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.ID@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}


}
