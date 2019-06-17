package com.ruegnerlukas.taskmanager.data.change;

public abstract class DataChange {


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




	public static DataChange createListChangeAdd(String identifierPath, Object addedObject) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ListChange(identifierPath, true, null, addedObject);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createListChangeAdd(strRemaining, addedObject));
		}
	}




	public static DataChange createListChangeRemove(String identifierPath, String removedIdentifier) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new ListChange(identifierPath, false, removedIdentifier, null);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createListChangeRemove(strRemaining, removedIdentifier));
		}
	}




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




	public static DataChange createMapChangeAdd(String identifierPath, Object key, Object value) {
		String[] tokens = identifierPath.split("/");
		if (tokens.length == 1) {
			return new MapChange(identifierPath, false, key, value);
		} else {
			String[] tokensRemaining = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, tokensRemaining, 0, tokens.length - 1);
			String strRemaining = String.join("/", tokensRemaining);
			return new NestedChange(tokens[0], createMapChangeAdd(strRemaining, key, value));
		}
	}




	private final String identifier;




	public DataChange(String identifier) {
		this.identifier = identifier;
	}




	public String getIdentifier() {
		return identifier;
	}


}
