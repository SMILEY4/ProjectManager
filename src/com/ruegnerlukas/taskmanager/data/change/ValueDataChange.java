package com.ruegnerlukas.taskmanager.data.change;

public class ValueDataChange extends DataChange {


	public final Object newValue;




	public ValueDataChange(String identifier, Object newValue) {
		super(identifier);
		this.newValue = newValue;

	}

}
