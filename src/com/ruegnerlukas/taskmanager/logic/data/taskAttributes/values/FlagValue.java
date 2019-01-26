package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;

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

}
