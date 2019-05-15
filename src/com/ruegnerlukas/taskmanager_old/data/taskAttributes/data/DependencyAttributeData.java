package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class DependencyAttributeData implements TaskAttributeData {


	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.DEPENDENCY;
	}




	@Override
	public boolean usesDefault() {
		return true;
	}




	@Override
	public TaskArrayValue getDefault() {
		return new TaskArrayValue();
	}




	@Override
	public TaskAttributeValue getValue(Var var) {
		return null;
	}




	@Override
	public DependencyAttributeData copy() {
		DependencyAttributeData copy = new DependencyAttributeData();
		return copy;
	}

}