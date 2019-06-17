package com.ruegnerlukas.taskmanager.data.change;

public class ValueChange extends DataChange {


	private Object newValue;




	public ValueChange(String identifier, Object newValue) {
		super(identifier);
		this.newValue = newValue;
	}




	public Object getNewValue() {
		return newValue;
	}

}
