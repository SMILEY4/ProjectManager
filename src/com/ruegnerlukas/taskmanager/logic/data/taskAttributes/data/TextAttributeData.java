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
	public boolean update(Var var, Object newValue) {

		switch (var) {

			case TEXT_CHAR_LIMIT: {
				if(newValue instanceof Integer) {
					charLimit = (Integer)newValue;
					if(defaultValue.length() > charLimit) {
						defaultValue = defaultValue.substring(0, charLimit);
					}
					return true;
				} else {
					return false;
				}
			}

			case TEXT_MULTILINE: {
				if(newValue instanceof Boolean) {
					multiline = (Boolean)newValue;
					if(multiline) {
						defaultValue = defaultValue.replaceAll(System.lineSeparator(), " ");
					}
					return true;
				} else {
					return false;
				}
			}


			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return true;
				} else {
					return false;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof String) {
					defaultValue = (String)newValue;
					return true;
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}
	}







}
