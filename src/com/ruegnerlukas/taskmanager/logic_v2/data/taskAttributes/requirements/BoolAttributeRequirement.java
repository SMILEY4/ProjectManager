package com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;

public class BoolAttributeRequirement implements TaskAttributeRequirement {

	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
	}




	@Override
	public boolean update(TaskAttributeRequirement data) {
		if(data instanceof BoolAttributeRequirement) {
			BoolAttributeRequirement updatedData = (BoolAttributeRequirement)data;

			// set new data
			this.useDefault = updatedData.useDefault;
			this.defaultValue = updatedData.defaultValue;
			return true;

		} else {
			return false;
		}
	}




	@Override
	public TaskAttributeRequirement copy() {
		BoolAttributeRequirement copy = new BoolAttributeRequirement();
		copy.useDefault = useDefault;
		copy.defaultValue = defaultValue;
		return copy;
	}

}
