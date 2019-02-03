package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;

import java.util.HashMap;
import java.util.Map;

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
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		Map<Var, TaskAttributeValue> changedVars = new HashMap<>();

		switch (var) {

			case TEXT_CHAR_LIMIT: {
				if (newValue instanceof NumberValue) {
					charLimit = ((NumberValue) newValue).getInt();
					changedVars.put(Var.TEXT_CHAR_LIMIT, newValue);
					if (defaultValue.length() > charLimit) {
						defaultValue = defaultValue.substring(0, charLimit);
						changedVars.put(Var.DEFAULT_VALUE, new TextValue(defaultValue));
					}
				}
				break;
			}

			case TEXT_MULTILINE: {
				if (newValue instanceof BoolValue) {
					multiline = ((BoolValue) newValue).getBoolValue();
					if (multiline && defaultValue.contains(System.lineSeparator())) {
						defaultValue = defaultValue.replaceAll(System.lineSeparator(), " ");
						changedVars.put(Var.DEFAULT_VALUE, new TextValue(defaultValue));
					}
					changedVars.put(Var.TEXT_MULTILINE, newValue);
				}
				break;
			}

			case USE_DEFAULT: {
				if (newValue instanceof BoolValue) {
					useDefault = ((BoolValue) newValue).getBoolValue();
					changedVars.put(Var.USE_DEFAULT, newValue);
				}
				break;
			}

			case DEFAULT_VALUE: {
				if (newValue instanceof TextValue) {
					defaultValue = ((TextValue) newValue).getText();
					changedVars.put(Var.DEFAULT_VALUE, newValue);
				}
				break;
			}

		}

		return changedVars;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !useDefault;
		} else {
			return value instanceof TextValue && (((TextValue) value).getText().length() <= charLimit);
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
