package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

import java.util.Collection;

public class TextArrayValue implements TaskAttributeValue {


	private String[] text;




	public TextArrayValue(String... text) {
		this.text = text;
	}


	public TextArrayValue(Collection<String> text) {
		this.text = new String[text.size()];
		int i = 0;
		for(String str : text) {
			this.text[i++] = str;
		}
	}



	public String[] getText() {
		return text;
	}




	@Override
	public Object getValue() {
		return text;
	}

}
