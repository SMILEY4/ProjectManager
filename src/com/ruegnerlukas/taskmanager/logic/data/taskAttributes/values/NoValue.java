package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

public class NoValue implements TaskAttributeValue {


	@Override
	public String toString() {
		return "NoValue";
	}




	@Override
	public Object getValue() {
		return null;
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


}
