package com.ruegnerlukas.taskmanager.data.change;

import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;

public abstract class DataChange {


	public enum ChangeType {
		VALUE, LIST, MAP, NESTED
	}




	/**
	 * @return a new {@link DataChange} ({@link ValueChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the new value as an {@link Object}.
	 * */
	public static DataChange createValueChange(String identifierPath, Object newObject) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ValueChange(identifierPath, newObject);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createValueChange(strRemaining, newObject));
		}
	}



	/**
	 * @return a new {@link DataChange} ({@link ListChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the added {@link Object}.
	 * */
	public static DataChange createListChangeAdd(String identifierPath, Object addedObject) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ListChange(identifierPath, true, addedObject);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createListChangeAdd(strRemaining, addedObject));
		}
	}



	/**
	 * @return a new {@link DataChange} ({@link ListChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the removed {@link SyncedElement} identified by the given identifier.
	 * */
	public static DataChange createListChangeRemove(String identifierPath, String removedIdentifier) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ListChange(identifierPath, false, null, removedIdentifier);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createListChangeRemove(strRemaining, removedIdentifier));
		}
	}




	/**
	 * @return a new {@link DataChange} ({@link ListChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the removed {@link Object}.
	 * */
	public static DataChange createListChangeRemove(String identifierPath, Object removedElement) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ListChange(identifierPath, false, removedElement);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createListChangeRemove(strRemaining, removedElement));
		}
	}




	/**
	 * @return a new {@link DataChange} ({@link MapChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the removed key.
	 * */
	public static DataChange createMapChangeRemove(String identifierPath, Object key) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new MapChange(identifierPath, false, key, null);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createMapChangeRemove(strRemaining, key));
		}
	}



	/**
	 * @return a new {@link DataChange} ({@link MapChange or {@link NestedChange}}) with the given path (single identifiers separated by "/") and the added key and object.
	 * */
	public static DataChange createMapChangeAdd(String identifierPath, Object key, Object value) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new MapChange(identifierPath, true, key, value);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createMapChangeAdd(strRemaining, key, value));
		}
	}




	private final ChangeType type;
	private final String identifier;




	public DataChange(ChangeType type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}




	public ChangeType getType() {
		return type;
	}




	public String getIdentifier() {
		return identifier;
	}


}
