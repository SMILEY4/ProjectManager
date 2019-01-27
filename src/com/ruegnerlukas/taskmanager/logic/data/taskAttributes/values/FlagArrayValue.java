package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;

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




	@Override
	public int compareTo(TaskAttributeValue o) {
		if(o instanceof FlagArrayValue) {
			final TaskFlag[] oValue = ((FlagArrayValue)o).getFlags();

			TaskFlag[] flags = ((FlagAttributeData) Logic.attribute.findAttribute(FlagAttributeData.NAME).data).flags;

			for(int i=0; i<Math.min(flags.length, oValue.length); i++) {
				TaskFlag tf = flags[i];
				TaskFlag of = oValue[i];

				int indexThis = 0;
				int indexOther = 0;

				for(int j=0; j<flags.length; j++) {
					if(tf == flags[j]) {
						indexThis = j;
					}
					if(of == flags[j]) {
						indexOther = j;
					}
				}

				if(indexThis != indexOther) {
					return indexThis - indexOther;
				}

			}

			return flags.length - oValue.length;

		} else {
			return +1;
		}
	}

}
