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




	@Override
	public String toString() {
		return text;
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TextValue textValue = (TextValue) o;
		return text != null ? text.equals(textValue.text) : textValue.text == null;
	}




	@Override
	public int hashCode() {
		return text != null ? text.hashCode() : 0;
	}



	@Override
	public int compareTo(TaskAttributeValue o) {
		if(o instanceof TextValue) {
			final String oValue = ((TextValue)o).getText();
			return this.text.compareTo(oValue);
		} else {
			return +1;
		}
	}
}
