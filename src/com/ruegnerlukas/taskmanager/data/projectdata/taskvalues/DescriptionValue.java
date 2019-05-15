package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

public class DescriptionValue extends TaskValue<String> {


	public DescriptionValue(String value) {
		super(value, AttributeType.DESCRIPTION);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((DescriptionValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Description@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
