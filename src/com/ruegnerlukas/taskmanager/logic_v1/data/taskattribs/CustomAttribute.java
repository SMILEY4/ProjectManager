package com.ruegnerlukas.taskmanager.logic_v1.data.taskattribs;



public class CustomAttribute {

	public static enum Type {
		TEXT, NUMBER, BOOLEAN, CHOICE, DATE
	}
	
	
	public static enum Variable {
		TEXT_CHAR_LIMIT, TEXT_USE_DEFAULT, TEXT_DEFAULT,
		NUMBER_DEC_PLACES, NUMBER_MIN, NUMBER_MAX, NUMBER_USE_DEFAULT, NUMBER_DEFAULT,
		BOOLEAN_USE_DEFAULT, BOOLEAN_DEFAULT,
		DATE_USE_DEFAULT, DATE_DEFAULT,
		CHOICE_VALUES, CHOICE_USE_DEFAULT, CHOICE_DEFAULT,
	}
	
	
	
	
	public String name;
	public CustomAttributeData data;
	
	
	public CustomAttribute(String name, Type type) {
		this.name = name;
		createNewData(type);
	}
	
	
	
	
	public void createNewData(Type type) {
		if(type == Type.TEXT) {
			data = new CustomTextAttributeData();
		}
		if(type == Type.NUMBER) {
			data = new CustomNumberAttributeData();
		}
		if(type == Type.BOOLEAN) {
			data = new CustomBoolAttributeData();
		}
		if(type == Type.CHOICE) {
			data = new CustomChoiceAttributeData();
		}
		if(type == Type.DATE) {
			data = new CustomDateAttributeData();
		}
	}

	
	
	
}
