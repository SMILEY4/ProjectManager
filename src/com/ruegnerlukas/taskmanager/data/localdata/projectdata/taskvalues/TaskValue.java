package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;

public abstract class TaskValue<T> {


	private final T value;
	private final AttributeType type;




	protected TaskValue(T value, AttributeType type) {
		this.value = value;
		this.type = type;
	}




	public AttributeType getAttType() {
		return type;
	}




	public T getValue() {
		return value;
	}




	@Override
	public boolean equals(Object other) {
		if (other instanceof TaskValue) {
			return compare((TaskValue) other) == 0;
		} else {
			return false;
		}
	}




	@Override
	public int hashCode() {
		return getValue() != null ? getValue().hashCode() : (getAttType() != null ? getAttType().hashCode() : 0);
	}




	public abstract String asDisplayableString();


	public abstract int compare(TaskValue<?> other);


}
