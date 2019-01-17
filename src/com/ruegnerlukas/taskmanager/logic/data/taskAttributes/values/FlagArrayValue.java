package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;

import java.util.Collection;

public class FlagArrayValue implements TaskAttributeValue {


	private TaskFlag[] flags;




	public FlagArrayValue(TaskFlag... flags) {
		this.flags = flags;
	}


	public FlagArrayValue(Collection<TaskFlag> flags) {
		this.flags = new TaskFlag[flags.size()];
		int i = 0;
		for(TaskFlag flag : flags) {
			this.flags[i++] = flag;
		}
	}



	public TaskFlag[] getFlags() {
		return flags;
	}




	@Override
	public Object getValue() {
		return flags;
	}

}
