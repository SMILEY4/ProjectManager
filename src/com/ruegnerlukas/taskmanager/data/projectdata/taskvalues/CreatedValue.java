package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

import java.time.LocalDateTime;

public class CreatedValue extends TaskValue<LocalDateTime> {


	public CreatedValue(LocalDateTime value) {
		super(value, AttributeType.CREATED);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((CreatedValue) other).getValue());
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Created@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}

}
