package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		Map<Var, TaskAttributeValue> changedVars = new HashMap<>();

		switch (var) {

			case FLAG_ATT_FLAGS: {
				if (newValue instanceof FlagArrayValue) {
					if (((FlagArrayValue) newValue).getFlags().length != 0) {

						this.flags = ((FlagArrayValue) newValue).getFlags();
						changedVars.put(Var.FLAG_ATT_FLAGS, newValue);

						boolean foundDefault = false;
						for (TaskFlag flag : flags) {
							if (flag == defaultFlag) {
								foundDefault = true;
								break;
							}
						}
						if (!foundDefault) {
							defaultFlag = flags[0];
							changedVars.put(Var.DEFAULT_VALUE, new FlagValue(defaultFlag));
						}
					}
				}
				break;
			}

			case DEFAULT_VALUE: {
				if (newValue instanceof FlagValue) {
					defaultFlag = ((FlagValue) newValue).getFlag();
					changedVars.put(Var.DEFAULT_VALUE, newValue);
				}
				break;
			}

		}

		return changedVars;
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
