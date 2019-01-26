package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;

import java.util.Arrays;
import java.util.Collection;

public class FlagArrayValue implements TaskAttributeValue {


	private TaskFlag[] flags;




	public FlagArrayValue(TaskFlag... flags) {
		this.flags = flags;
	}




	public FlagArrayValue(Collection<TaskFlag> flags) {
		this.flags = new TaskFlag[flags.size()];
		int i = 0;
		for (TaskFlag flag : flags) {
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




	@Override
	public String toString() {
		return Arrays.toString(flags);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FlagArrayValue that = (FlagArrayValue) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(flags, that.flags);
	}




	@Override
	public int hashCode() {
		return Arrays.hashCode(flags);
	}

}
