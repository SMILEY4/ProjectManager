package com.ruegnerlukas.taskmanager.data.externaldata;

public class DataChange {


	public final String identifier;
	public final Object oldValue;
	public final Object newValue;




	public DataChange(String identifier, Object oldValue, Object newValue) {
		this.identifier = identifier;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

}
