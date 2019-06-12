package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;

public class ChoiceValue extends TaskValue<String> {


	public ChoiceValue(String value) {
		super(value, AttributeType.CHOICE);
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
			return this.getValue().compareTo(((ChoiceValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Choice@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
