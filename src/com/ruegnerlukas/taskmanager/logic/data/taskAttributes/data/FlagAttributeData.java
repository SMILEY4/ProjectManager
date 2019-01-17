package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

public class FlagAttributeData implements TaskAttributeData {


	public TaskFlag[] flags;
	public TaskFlag defaultFlag;



	public FlagAttributeData() {
		defaultFlag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Default", false);
		flags = new TaskFlag[] {defaultFlag};
	}




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.FLAG;
	}




	@Override
	public Var[] update(Var var, TaskAttributeValue newValue) {

		switch (var) {

			case FLAG_ATT_FLAGS: {
				if(newValue instanceof FlagArrayValue) {
					if(((FlagArrayValue)newValue).getFlags().length == 0) {
						return null;
					}

					flags = ((FlagArrayValue)newValue).getFlags();

					boolean foundDefault = false;
					for(TaskFlag flag : flags) {
						if(flag == defaultFlag) {
							foundDefault = true;
							break;
						}
					}
					if(!foundDefault) {
						defaultFlag = flags[0];
						return new Var[] {Var.FLAG_ATT_FLAGS, Var.DEFAULT_VALUE};
					} else {
						return new Var[] {Var.FLAG_ATT_FLAGS};
					}

				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof FlagValue) {
					defaultFlag = ((FlagValue)newValue).getFlag();
					return new Var[] {Var.DEFAULT_VALUE};
				} else {
					return null;
				}
			}

			default: {
				return null;
			}
		}
	}

}
