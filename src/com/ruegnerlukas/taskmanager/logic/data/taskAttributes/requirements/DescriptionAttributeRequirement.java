package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class DescriptionAttributeRequirement implements TaskAttributeRequirement {

	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DESCRIPTION;
	}


	@Override
	public boolean update(TaskAttributeRequirement data) {
		return false;
	}




	@Override
	public TaskAttributeRequirement copy() {
		return new DescriptionAttributeRequirement();
	}


}
