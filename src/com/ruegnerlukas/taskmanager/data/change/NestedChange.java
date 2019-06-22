package com.ruegnerlukas.taskmanager.data.change;

public class NestedChange extends DataChange {


	private final DataChange next;




	public NestedChange(String identifier, DataChange next) {
		super(identifier);
		this.next = next;
	}




	public DataChange getNext() {
		return next;
	}




	@Override
	public String toString() {
		return "NestedChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + "  ->  " + getNext().toString();
	}

}