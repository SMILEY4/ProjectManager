package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

import java.time.LocalDate;

public class DateValue extends TaskValue<LocalDate> {


	public DateValue(LocalDate value) {
		super(value, AttributeType.DATE);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((DateValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Date@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
