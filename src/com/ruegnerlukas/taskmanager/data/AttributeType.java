package com.ruegnerlukas.taskmanager.data;

public enum AttributeType {

	ID("ID", true),
	DESCRIPTION("Description", true),
	CREATED("Created", true),
	LAST_UPDATED("Last Updated", true),
	FLAG("Flag", true),
	TEXT("Text", false),
	NUMBER("Number", false),
	BOOLEAN("Boolean", false),
	CHOICE("Choice", false),
	DATE("Date", false),
	DEPENDENCY("Dependency", false);

	public final String display;
	public final boolean fixed;




	AttributeType(String display, boolean fixed) {
		this.display = display;
		this.fixed = fixed;
	}




	public static AttributeType[] getFixedTypes() {
		int n = 0;
		for (AttributeType type : AttributeType.values()) {
			if (type.fixed) n++;
		}
		AttributeType[] types = new AttributeType[n];
		for (int i = 0, j = 0; i < AttributeType.values().length; i++) {
			if (AttributeType.values()[i].fixed) types[j++] = AttributeType.values()[i];
		}
		return types;
	}




	public static AttributeType[] getFreeTypes() {
		int n = 0;
		for (AttributeType type : AttributeType.values()) {
			if (!type.fixed) n++;
		}
		AttributeType[] types = new AttributeType[n];
		for (int i = 0, j = 0; i < AttributeType.values().length; i++) {
			if (!AttributeType.values()[i].fixed) types[j++] = AttributeType.values()[i];
		}
		return types;
	}


}
