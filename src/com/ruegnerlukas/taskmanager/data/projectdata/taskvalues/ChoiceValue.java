package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

public class ChoiceValue extends TaskValue<String> {


	public ChoiceValue(String value) {
		super(value, AttributeType.CHOICE);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((ChoiceValue) other).getValue());
		}
	}

}
