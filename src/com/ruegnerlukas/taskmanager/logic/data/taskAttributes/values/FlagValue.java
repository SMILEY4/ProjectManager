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

}
