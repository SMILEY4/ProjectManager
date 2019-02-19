package com.ruegnerlukas.taskmanager.data.taskAttributes;

public enum TaskAttributeType {

	FLAG("Flag", Type.FIXED),
	ID("ID", Type.FIXED),
	DESCRIPTION("Description", Type.FIXED),

	NUMBER("Number", Type.CUSTOM),
	TEXT("Text", Type.CUSTOM),
	BOOLEAN("Boolean", Type.CUSTOM),
	CHOICE("Choice", Type.CUSTOM),
	DEPENDENCY("Dependency", Type.CUSTOM);





	public enum Type {
		FIXED,
		CUSTOM
	}






	public final String display;
	public final Type type;




	TaskAttributeType(String display, Type type) {
		this.display = display;
		this.type = type;
	}




	public static TaskAttributeType getFromDisplay(String display) {
		for (TaskAttributeType type : TaskAttributeType.values()) {
			if (type.display.equalsIgnoreCase(display)) {
				return type;
			}
		}
		return null;
	}


}
