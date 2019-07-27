package com.ruegnerlukas.taskmanager.data.change;

public class NestedChange extends DataChange {


	private final DataChange next;




	public NestedChange(String identifier, DataChange next) {
		super(ChangeType.NESTED, identifier);
		this.next = next;
	}




	/**
	 * @return the next {@link DataChange} in the chain.
	 */
	public DataChange getNext() {
		return next;
	}




	@Override
	public String toString() {
		return "NestedChange@" + Integer.toHexString(this.hashCode()) + ": " + getIdentifier() + "  ->  " + getNext().toString();
	}

}
