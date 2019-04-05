package com.ruegnerlukas.taskmanager_old.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.updaters.AttributeUpdater;

import java.util.Map;

public class DescriptionAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		return false;
	}


}
