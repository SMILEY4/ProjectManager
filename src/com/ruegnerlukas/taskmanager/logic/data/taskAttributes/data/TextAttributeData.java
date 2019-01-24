package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.*;

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
	public Var[] update(Var var, TaskAttributeValue newValue) {

		switch (var) {

			case TEXT_CHAR_LIMIT: {
				if(newValue instanceof NumberValue) {
					charLimit = ((NumberValue)newValue).getInt();
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
				if(newValue instanceof BoolValue) {
					multiline = ((BoolValue)newValue).getBoolValue();
					if(multiline) {
						defaultValue = defaultValue.replaceAll(System.lineSeparator(), " ");
					}
					return new Var[] {Var.TEXT_MULTILINE};
				} else {
					return null;
				}
			}


			case USE_DEFAULT: {
				if(newValue instanceof BoolValue) {
					useDefault = ((BoolValue)newValue).getBoolValue();
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof TextValue) {
					defaultValue = ((TextValue)newValue).getText();
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




	@Override
	public boolean validate(TaskAttributeValue value) {
		if(value instanceof NoValue) {
			return !useDefault;
		} else {
			return value instanceof TextValue && (((TextValue)value).getText().length() <= charLimit);
		}
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public TextValue getDefault() {
		return new TextValue(defaultValue);
	}

}
