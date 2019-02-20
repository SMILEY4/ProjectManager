package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class ChoiceAttributeData implements TaskAttributeData {


	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
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
