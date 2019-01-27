package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

public class NoValue implements TaskAttributeValue {


	@Override
	public Object getValue() {
		return null;
	}




	@Override
	public String toString() {
		return "NoValue";
	}




	@Override
	public int hashCode() {
		return "NoValue".hashCode();
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		return getClass() == o.getClass();
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		return 0;
	}

}
