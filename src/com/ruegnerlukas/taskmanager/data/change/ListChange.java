package com.ruegnerlukas.taskmanager.data.change;

import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;

public class ListChange extends DataChange {


	private final boolean wasAdded;
	private final Object element;
	private final String identifierRemoved;




	public ListChange(String identifier, boolean wasAdded, Object element) {
		this(identifier, wasAdded, element, null);
	}




	public ListChange(String identifier, boolean wasAdded, Object element, String identifierRemoved) {
		super(ChangeType.LIST, identifier);
		this.element = element;
		this.wasAdded = wasAdded;
		this.identifierRemoved = identifierRemoved;
	}




	public boolean wasAdded() {
		return wasAdded;
	}




	public boolean wasRemoved() {
		return !wasAdded;
	}




	public Object getAdded() {
		return wasAdded() ? element : null;
	}




	public String getAddedIdentifier() {
		return wasAdded() && element instanceof SyncedElement ? ((SyncedElement) element).getNode().identifier : null;
	}




	public Object getRemoved() {
		return wasRemoved() && identifierRemoved == null ? element : null;
	}




	public String getRemovedIdentifier() {
		return wasRemoved() && identifierRemoved != null ? identifierRemoved : null;
	}




	public boolean removeByIdentifier() {
		return wasRemoved() && identifierRemoved != null;
	}




	@Override
	public String toString() {
		return "ListChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + " = " + (wasAdded ? "added" : "removed") + " " + element;
	}

}
