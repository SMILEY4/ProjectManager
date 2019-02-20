package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Arrays;
import java.util.HashSet;

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
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof FlagValue) {
			return new HashSet<>(Arrays.asList(flags)).contains(((FlagValue) value).getFlag());
		} else {
			return false;
		}
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
