package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class TextAttributeData implements TaskAttributeData {

	public int charLimit = 64;
	public boolean multiline = false;
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.TEXT;
	}




	@Override
	public Var[] update(Var var, Object newValue) {

		switch (var) {

			case TEXT_CHAR_LIMIT: {
				if(newValue instanceof Integer) {
					charLimit = (Integer)newValue;
					if(defaultValue.length() > charLimit) {
						defaultValue = defaultValue.substring(0, charLimit);
						return new Var[] {Var.TEXT_CHAR_LIMIT, Var.DEFAULT_VALUE};
					} else {
						return new Var[] {Var.TEXT_CHAR_LIMIT};
					}
				} else {
					return null;
				}
			}

			case TEXT_MULTILINE: {
				if(newValue instanceof Boolean) {
					multiline = (Boolean)newValue;
					if(multiline) {
						defaultValue = defaultValue.replaceAll(System.lineSeparator(), " ");
					}
					return new Var[] {Var.TEXT_MULTILINE};
				} else {
					return null;
				}
			}


			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof String) {
					defaultValue = (String)newValue;
					return new Var[] {Var.DEFAULT_VALUE};
				} else {
					return null;
				}
			}

			default: {
				return null;
			}
		}
	}







}
