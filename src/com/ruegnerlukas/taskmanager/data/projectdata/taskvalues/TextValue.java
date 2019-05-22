package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

public class TextValue extends TaskValue<String> {


	public TextValue(String value) {
		super(value, AttributeType.TEXT);
	}




	@Override
	public String asDisplayableString() {
		return getValue();
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? 1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((TextValue) other).getValue());
		}
	}





	@Override
	public String toString() {
		return "TaskValue.Text@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
