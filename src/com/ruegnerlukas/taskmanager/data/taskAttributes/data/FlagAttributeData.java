package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;

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




	@Override
	public TaskAttributeValue getValue(Var var) {
		if (var == Var.FLAG_ATT_FLAGS) {
			return new FlagArrayValue(flags);
		}
		if (var == Var.DEFAULT_VALUE) {
			return getDefault();
		}
		return null;
	}




	@Override
	public FlagAttributeData copy() {
		FlagAttributeData copy = new FlagAttributeData();
		copy.flags = this.flags;
		copy.defaultFlag = this.defaultFlag;
		return copy;
	}


}
