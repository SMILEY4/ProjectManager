package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class DescriptionAttributeData implements TaskAttributeData {


	public static final String NAME = "Description Attribute";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DESCRIPTION;
	}




	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public TextValue getDefault() {
		return new TextValue("");
	}




	@Override
	public TaskAttributeValue getValue(Var var) {
		return null;
	}




	@Override
	public DescriptionAttributeData copy() {
		DescriptionAttributeData copy = new DescriptionAttributeData();
		return copy;
	}

}
