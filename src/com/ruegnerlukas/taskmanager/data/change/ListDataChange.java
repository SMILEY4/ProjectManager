package com.ruegnerlukas.taskmanager.data.change;

public class ListDataChange extends DataChange {


	public final boolean wasAdded;
	public final boolean wasRemoved;
	public final Object object;




	public ListDataChange(String identifier, boolean wasAdded, boolean wasRemoved, Object object) {
		super(identifier);
		this.wasAdded = wasAdded;
		this.wasRemoved = wasRemoved;
		this.object = object;
	}

}
