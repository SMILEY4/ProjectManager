package com.ruegnerlukas.taskmanager.data.change;

public class MapDataChange extends DataChange {


	public final boolean wasRemoved;
	public final boolean wasAdded;
	public final boolean wasChanged;
	public final Object key;
	public final Object value;




	public MapDataChange(String identifier, boolean wasRemoved, boolean wasAdded, boolean wasChanged, Object key, Object object) {
		super(identifier);
		this.wasRemoved = wasRemoved;
		this.wasAdded = wasAdded;
		this.wasChanged = wasChanged;
		this.key = key;
		this.value = object;
	}


}
