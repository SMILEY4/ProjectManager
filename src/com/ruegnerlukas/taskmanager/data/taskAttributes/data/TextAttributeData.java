package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class TextAttributeData implements TaskAttributeData {


	public int charLimit = 64;
	public boolean multiline = false;
	public int nLinesExpected = 2;
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.TEXT;
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
