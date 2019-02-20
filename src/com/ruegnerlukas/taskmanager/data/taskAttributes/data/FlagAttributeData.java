package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;

public class FlagAttributeData implements TaskAttributeData {


	public static final String NAME = "Flag Attribute";


	public TaskFlag[] flags;
	public TaskFlag defaultFlag;




	public FlagAttributeData() {
		defaultFlag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Default");
		flags = new TaskFlag[]{defaultFlag};
	}




	public boolean hasFlag(TaskFlag flag) {
		for (TaskFlag f : flags) {
			if (f == flag) {
				return true;
			}
		}
		return false;
	}




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.FLAG;
	}




	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public FlagValue getDefault() {
		return new FlagValue(defaultFlag);
	}

}
