package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TextValue;

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
		return value instanceof TextValue;
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
