package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlagAttributeRequirement implements TaskAttributeRequirement {


	public List<TaskFlag> flags = new ArrayList<>();
	public TaskFlag defaultFlag;



	public FlagAttributeRequirement() {
		defaultFlag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Default", false);
		flags.add(defaultFlag);
	}



	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.FLAG;
	}




	@Override
	public boolean update(TaskAttributeRequirement data) {
		if(data instanceof FlagAttributeRequirement) {
			FlagAttributeRequirement updatedData = (FlagAttributeRequirement)data;

			// validate new data
			if(updatedData.flags.size() <= 0) {
				return false;
			}
			if(defaultFlag == null) {
				return false;
			}
			Set<String> setNames = new HashSet<>();
			for(TaskFlag flag : updatedData.flags) {
				setNames.add(flag.name);
			}
			if(setNames.size() != updatedData.flags.size()) {
				return false;
			}

			// set new data
			this.flags.clear();
			this.flags.addAll(updatedData.copyFlags());
			this.defaultFlag = updatedData.defaultFlag;
			return true;

		} else {
			return false;
		}
	}




	@Override
	public TaskAttributeRequirement copy() {
		FlagAttributeRequirement copy = new FlagAttributeRequirement();
		copy.flags.clear();
		copy.flags.addAll(copyFlags());
		copy.defaultFlag = defaultFlag;
		return copy;
	}




	private List<TaskFlag> copyFlags() {
		List<TaskFlag> listCopy = new ArrayList<>(flags.size());
		for(TaskFlag flag : flags) {
			listCopy.add(new TaskFlag(flag.color, flag.name, flag.isDefaultFlag));
		}
		return listCopy;
	}

}
