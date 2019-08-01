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




	/**
	 * @return true, of an entry was added to the map.
	 */
	public boolean wasAdded() {
		return wasAdded;
	}




	/**
	 * @return true, of an entry was removed from the map.
	 */
	public boolean wasRemoved() {
		return !wasAdded;
	}




	/**
	 * @return the key of the added entry (or null if removed)
	 */
	public Object getAddedKey() {
		return wasAdded() ? key : null;
	}




	/**
	 * @return the value of the added entry (or null if removed)
	 */
	public Object getAddedValue() {
		return wasAdded() ? value : null;
	}




	/**
	 * @return the identifier of the added value (or not if element was removed or is not a {@link SyncedElement}).
	 */
	public String getAddedIdentifier() {
		return wasAdded() && value instanceof SyncedElement ? ((SyncedElement) value).getNode().identifier : null;
	}




	/**
	 * @return the key of the removed entry (or null if added)
	 */
	public Object getRemovedKey() {
		return wasRemoved() ? key : null;
	}




	/**
	 * @return the value of the removed entry (or null if added)
	 */
	public Object getRemovedValue() {
		return wasRemoved() ? value : null;
	}




	@Override
	public String toString() {
		return "MapChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + " = " + (wasAdded ? "added" : "removed");
	}

}
