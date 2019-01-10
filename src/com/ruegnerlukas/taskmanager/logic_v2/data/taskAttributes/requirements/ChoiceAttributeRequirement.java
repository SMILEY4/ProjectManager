package com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;

import java.util.HashSet;
import java.util.Set;

public class ChoiceAttributeRequirement implements TaskAttributeRequirement {

	public Set<String> values = new HashSet<String>();
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public boolean update(TaskAttributeRequirement data) {
		if(data instanceof ChoiceAttributeRequirement) {
			ChoiceAttributeRequirement updatedData = (ChoiceAttributeRequirement)data;

			// validate new data
			if(!updatedData.values.contains(updatedData.defaultValue)) {
				return false;
			}

			// set new data
			this.values.clear();
			this.values.addAll(updatedData.values);
			this.useDefault = updatedData.useDefault;
			this.defaultValue = updatedData.defaultValue;
			return true;

		} else {
			return false;
		}
	}




	@Override
	public TaskAttributeRequirement copy() {
		ChoiceAttributeRequirement copy = new ChoiceAttributeRequirement();
		copy.values.addAll(values);
		copy.useDefault = useDefault;
		copy.defaultValue = defaultValue;
		return copy;
	}

}
