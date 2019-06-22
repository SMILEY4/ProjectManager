package com.ruegnerlukas.taskmanager.data.change;

public class MapChange extends DataChange {


	public final boolean wasAdded;
	public final Object key;
	public final Object value;




	public MapChange(String identifier, boolean wasAdded, Object key, Object value) {
		super(identifier);
		this.wasAdded = wasAdded;
		this.key = key;
		this.value = value;
	}




	@Override
	public String toString() {
		return "MapChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + " = " + (wasAdded ? "added" : "removed");
	}

}
