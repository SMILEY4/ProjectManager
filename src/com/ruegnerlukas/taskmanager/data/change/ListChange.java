package com.ruegnerlukas.taskmanager.data.change;

public class ListChange extends DataChange {


	public final boolean wasAdded;
	public final String removedIdentifier;
	public final Object objAdded;




	public ListChange(String identifier, boolean wasAdded, String removedIdentifier, Object objAdded) {
		super(identifier);
		this.removedIdentifier = removedIdentifier;
		this.objAdded = objAdded;
		this.wasAdded = wasAdded;
	}


}
