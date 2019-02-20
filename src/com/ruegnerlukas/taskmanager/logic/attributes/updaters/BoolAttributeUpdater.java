package com.ruegnerlukas.taskmanager.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Map;

public class BoolAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		if (data.getType() == TaskAttributeType.BOOLEAN) {
			BoolAttributeData boolData = (BoolAttributeData) data;

			switch (var) {
				case USE_DEFAULT: {
					return setUseDefault(boolData, newValue, changedVars);
				}
				case DEFAULT_VALUE: {
					return setDefaultValue(boolData, newValue, changedVars);
				}
				default: {
					return false;
				}
			}

		} else {
			return false;
		}

	}




	private boolean setUseDefault(BoolAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.useDefault = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.USE_DEFAULT, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setDefaultValue(BoolAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.defaultValue = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.DEFAULT_VALUE, value);
			return true;
		} else {
			return false;
		}
	}


}
