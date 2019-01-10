package com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;

public class IDAttributeRequirement implements TaskAttributeRequirement {

	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.ID;
	}


	@Override
	public boolean update(TaskAttributeRequirement data) {
		return false;
	}




	@Override
	public TaskAttributeRequirement copy() {
		return new IDAttributeRequirement();
	}

}
