package com.ruegnerlukas.taskmanager.data.change;

import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;

public class MapChange extends DataChange {


	public final boolean wasAdded;
	public final Object key;
	public final Object value;




	public MapChange(String identifier, boolean wasAdded, Object key, Object value) {
		super(ChangeType.MAP, identifier);
		this.wasAdded = wasAdded;
		this.key = key;
		this.value = value;
	}




	public boolean wasAdded() {
		return wasAdded;
	}




	public boolean wasRemoved() {
		return !wasAdded;
	}




	public Object getAddedKey() {
		return wasAdded() ? key : null;
	}




	public Object getAddedValue() {
		return wasAdded() ? value : null;
	}




	public String getAddedIdentifier() {
		return wasAdded() && value instanceof SyncedElement ? ((SyncedElement) value).getNode().identifier : null;
	}




	public Object getRemovedKey() {
		return wasRemoved() ? key : null;
	}




	public Object getRemovedValue() {
		return wasRemoved() ? key : null;
	}




	@Override
	public String toString() {
		return "MapChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + " = " + (wasAdded ? "added" : "removed");
	}

}
