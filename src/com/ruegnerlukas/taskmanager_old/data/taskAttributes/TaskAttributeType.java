package com.ruegnerlukas.taskmanager_old.data.taskAttributes;

public enum TaskAttributeType {

	FLAG("Flag", Type.FIXED, true),
	ID("ID", Type.FIXED, true),
	DESCRIPTION("Description", Type.FIXED, true),
	NUMBER("Number", Type.CUSTOM, true),
	TEXT("Text", Type.CUSTOM, true),
	BOOLEAN("Boolean", Type.CUSTOM, true),
	CHOICE("Choice", Type.CUSTOM, true),
	DEPENDENCY("Dependency", Type.CUSTOM, false);






	public enum Type {
		FIXED,
		CUSTOM
	}






	public final String display;
	public final Type type;
	public final boolean groupable;




	TaskAttributeType(String display, Type type, boolean groupable) {
		this.display = display;
		this.type = type;
		this.groupable = groupable;
	}




	public static TaskAttributeType getFromDisplay(String display) {
		for (TaskAttributeType type : TaskAttributeType.values()) {
			if (type.display.equalsIgnoreCase(display)) {
				return type;
			}
		}
		return null;
	}




	public boolean fixed() {
		return this.type == Type.FIXED;
	}

}
