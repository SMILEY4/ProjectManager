package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreatedValue extends TaskValue<LocalDateTime> {


	public CreatedValue(LocalDateTime value) {
		super(value, AttributeType.CREATED);
	}




	@Override
	public String asDisplayableString() {
		return getValue().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? 1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((CreatedValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Created@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
