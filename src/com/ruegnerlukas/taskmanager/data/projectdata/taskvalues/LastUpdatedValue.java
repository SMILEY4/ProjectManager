package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;

import java.time.LocalDateTime;

public class LastUpdatedValue extends TaskValue<LocalDateTime> {


	public LastUpdatedValue(LocalDateTime value) {
		super(value, AttributeType.LAST_UPDATED);
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			return this.getValue().compareTo(((LastUpdatedValue) other).getValue());
		}
	}

}
