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




	/**
	 * @return true, of an element was added to the list.
	 */
	public boolean wasAdded() {
		return wasAdded;
	}




	/**
	 * @return true, of an element was removed from the list.
	 */
	public boolean wasRemoved() {
		return !wasAdded;
	}




	/**
	 * @return the added element or null (if element was removed)
	 */
	public Object getAdded() {
		return wasAdded() ? element : null;
	}




	/**
	 * @return the identifier of the added element or null (if element was removed or added element was removed by an identifier).
	 */
	public String getAddedIdentifier() {
		return wasAdded() && element instanceof SyncedElement ? ((SyncedElement) element).getNode().identifier : null;
	}




	/**
	 * @return the removed element or null (if element was added)
	 */
	public Object getRemoved() {
		return wasRemoved() ? element : null;
	}




	/**
	 * @return the identifier of the removed element or null (if element was added or removed element was not removed by an identifier).
	 */
	public String getRemovedIdentifier() {
		return wasRemoved() && identifierRemoved != null ? identifierRemoved : null;
	}




	/**
	 * @return true, if an element was removed by an identifier.
	 */
	public boolean removeByIdentifier() {
		return wasRemoved() && identifierRemoved != null;
	}




	@Override
	public String toString() {
		return "ListChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + " = " + (wasAdded ? "added" : "removed") + " " + element;
	}

}
