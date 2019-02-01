package com.ruegnerlukas.taskmanager.data.taskAttributes;

public enum TaskAttributeType {

	FLAG("Flag", true),
	ID("ID", true),
	DESCRIPTION("Description", true),

	NUMBER("Number", false),
	TEXT("Text", false),
	BOOLEAN("Boolean", false),
	CHOICE("Choice", false);



	public final String display;
	public final boolean fixed;

	private TaskAttributeType(String display, boolean fixed) {
		this.display = display;
		this.fixed = fixed;
	}


	public static TaskAttributeType getFromDisplay(String display) {
		for(TaskAttributeType type : TaskAttributeType.values()) {
			if(type.display.equalsIgnoreCase(display)) {
				return type;
			}
		}
		return null;
	}

}
