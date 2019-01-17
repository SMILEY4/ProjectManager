package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

public class TextValue implements TaskAttributeValue {


	private String text;




	public TextValue(String text) {
		this.text = text;
	}




	public String getText() {
		return text;
	}




	@Override
	public Object getValue() {
		return text;
	}

}
