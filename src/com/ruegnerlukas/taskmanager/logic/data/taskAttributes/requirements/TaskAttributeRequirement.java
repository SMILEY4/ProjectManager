package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements;


import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public interface TaskAttributeRequirement {
	
	public TaskAttributeType getType();

	public boolean update(TaskAttributeRequirement updatedData);

	public TaskAttributeRequirement copy();

}
