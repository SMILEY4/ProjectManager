package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class DescriptionAttributeData implements TaskAttributeData {


	public static final String NAME = "Description Attribute";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DESCRIPTION;
	}




	@Override
	public Var[] update(Var var, TaskAttributeValue newValue) {
		return null;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return true;
		} else {
			return value instanceof TextValue;
		}
	}





	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public TextValue getDefault() {
		return new TextValue("");
	}

}
