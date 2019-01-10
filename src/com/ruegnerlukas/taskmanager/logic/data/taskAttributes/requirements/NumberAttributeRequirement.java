package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class NumberAttributeRequirement implements TaskAttributeRequirement {

	public int decPlaces = 0;
	public double min = -100;
	public double max = +100;
	public boolean useDefault = false;
	public double defaultValue = 0;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.NUMBER;
	}




	@Override
	public boolean update(TaskAttributeRequirement data) {
		if(data instanceof NumberAttributeRequirement) {
			NumberAttributeRequirement updatedData = (NumberAttributeRequirement)data;

			// validate new data
			if(updatedData.decPlaces < 0) {
				return false;
			}
			if(updatedData.min > updatedData.max) {
				return false;
			}
			if(updatedData.min > updatedData.defaultValue || updatedData.defaultValue > updatedData.max ) {
				return false;
			}

			// set new data
			this.decPlaces = updatedData.decPlaces;
			this.min = updatedData.min;
			this.max = updatedData.max;
			this.useDefault = updatedData.useDefault;
			this.defaultValue = updatedData.defaultValue;
			return true;

		} else {
			return false;
		}
	}




	@Override
	public TaskAttributeRequirement copy() {
		NumberAttributeRequirement copy = new NumberAttributeRequirement();
		copy.decPlaces = decPlaces;
		copy.min = min;
		copy.max = max;
		copy.useDefault = useDefault;
		copy.defaultValue = defaultValue;
		return copy;
	}

}
