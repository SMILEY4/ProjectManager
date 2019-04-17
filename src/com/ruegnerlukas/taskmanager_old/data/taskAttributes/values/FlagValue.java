package com.ruegnerlukas.taskmanager_old.data.taskAttributes.values;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;

public class FlagValue implements TaskAttributeValue {


	private TaskFlag flag;




	public FlagValue(TaskFlag flag) {
		this.flag = flag;
	}




	public TaskFlag getFlag() {
		return flag;
	}




	@Override
	public Object getValue() {
		return flag;
	}




	@Override
	public String toString() {
		return flag.name;
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FlagValue flagValue = (FlagValue) o;

		return flag != null ? flag.equals(flagValue.flag) : flagValue.flag == null;
	}




	@Override
	public int hashCode() {
		return flag != null ? flag.hashCode() : 0;
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		if (o instanceof FlagValue) {
			final TaskFlag oValue = ((FlagValue) o).getFlag();

			int indexThis = 0;
			int indexOther = 0;

			TaskFlag[] flags = Logic.taskFlags.getAllFlags().getValue();

			for (int i = 0; i < flags.length; i++) {
				if (this.getFlag() == flags[i]) {
					indexThis = i;
				}
				if (oValue == flags[i]) {
					indexOther = i;
				}
			}

			return MathUtils.clamp(indexThis - indexOther, -1, +1);

		} else {
			return -2;
		}
	}

}