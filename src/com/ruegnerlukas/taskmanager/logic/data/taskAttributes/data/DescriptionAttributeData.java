package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

public class DescriptionAttributeData implements TaskAttributeData {


	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DESCRIPTION;
	}




	@Override
	public Var[] update(Var var, TaskAttributeValue newValue) {
		return null;
	}


}
