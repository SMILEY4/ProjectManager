package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

import java.util.Arrays;
import java.util.HashSet;

public class ChoiceAttributeData implements TaskAttributeData {


	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !useDefault;
		} else {
			if (value instanceof TextValue) {
				return new HashSet<>(Arrays.asList(values)).contains(((TextValue) value).getText());
			} else {
				return false;
			}
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
