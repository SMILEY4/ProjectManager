package com.ruegnerlukas.taskmanager_old.logic.attributes.updaters;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.updaters.AttributeUpdater;

import java.util.Map;

public class NumberAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		if (data.getType() == TaskAttributeType.NUMBER) {
			NumberAttributeData numberData = (NumberAttributeData) data;

			switch (var) {
				case NUMBER_ATT_DEC_PLACES: {
					return setDecPlaces(numberData, newValue, changedVars);
				}
				case NUMBER_ATT_MIN: {
					return setMin(numberData, newValue, changedVars);
				}
				case NUMBER_ATT_MAX: {
					return setMax(numberData, newValue, changedVars);
				}
				case USE_DEFAULT: {
					return setUseDefault(numberData, newValue, changedVars);
				}
				case DEFAULT_VALUE: {
					return setDefaultValue(numberData, newValue, changedVars);
				}
				default: {
					return false;
				}
			}

		} else {
			return false;
		}

	}




	private boolean setDecPlaces(NumberAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.decPlaces = ((NumberValue) value).getInt();
			changedVars.put(TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES, value);
			setMin(data, new NumberValue(MathUtils.setDecPlaces(data.min, data.decPlaces)), changedVars);
			setMax(data, new NumberValue(MathUtils.setDecPlaces(data.max, data.decPlaces)), changedVars);
			setDefaultValue(data, new NumberValue(MathUtils.setDecPlaces(data.defaultValue, data.decPlaces)), changedVars);
			return true;
		} else {
			return false;
		}
	}




	private boolean setMin(NumberAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.min = ((NumberValue) value).getDouble();
			changedVars.put(TaskAttributeData.Var.NUMBER_ATT_MIN, value);
			if (data.defaultValue < data.min) {
				setDefaultValue(data, value, changedVars);
			}
			return true;
		} else {
			return false;
		}
	}




	private boolean setMax(NumberAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.max = ((NumberValue) value).getDouble();
			changedVars.put(TaskAttributeData.Var.NUMBER_ATT_MAX, value);
			if (data.defaultValue > data.max) {
				setDefaultValue(data, value, changedVars);
			}
			return true;
		} else {
			return false;
		}
	}




	private boolean setUseDefault(NumberAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.useDefault = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.USE_DEFAULT, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setDefaultValue(NumberAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.defaultValue = ((NumberValue) value).getDouble();
			changedVars.put(TaskAttributeData.Var.DEFAULT_VALUE, value);
			return true;
		} else {
			return false;
		}
	}


}
