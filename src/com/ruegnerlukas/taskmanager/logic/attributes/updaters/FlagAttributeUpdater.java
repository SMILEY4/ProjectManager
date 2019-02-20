package com.ruegnerlukas.taskmanager.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Map;

public class FlagAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		if (data.getType() == TaskAttributeType.FLAG) {
			FlagAttributeData flagData = (FlagAttributeData) data;

			switch (var) {

				case FLAG_ATT_FLAGS: {
					return setFlags(flagData, newValue, changedVars);
				}
				case DEFAULT_VALUE: {
					return setDefaultValue(flagData, newValue, changedVars);
				}
				default: {
					return false;
				}
			}

		} else {
			return false;
		}

	}




	private boolean setFlags(FlagAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof FlagArrayValue) {

			TaskFlag[] flags = ((FlagArrayValue) value).getFlags();
			if (flags.length == 0) {
				return false;
			}
			data.flags = flags;
			changedVars.put(TaskAttributeData.Var.FLAG_ATT_FLAGS, value);

			boolean containsCurrentDefault = false;
			for (TaskFlag flag : flags) {
				if (flag == data.defaultFlag) {
					containsCurrentDefault = true;
					break;
				}
			}

			if (!containsCurrentDefault) {
				setDefaultValue(data, new FlagValue(flags[0]), changedVars);
			}

			return true;
		} else {
			return false;
		}
	}




	private boolean setDefaultValue(FlagAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof FlagValue) {
			data.defaultFlag = ((FlagValue) value).getFlag();
			changedVars.put(TaskAttributeData.Var.DEFAULT_VALUE, value);
			return true;
		} else {
			return false;
		}
	}


}
