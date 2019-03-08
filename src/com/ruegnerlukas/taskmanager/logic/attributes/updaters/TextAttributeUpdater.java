package com.ruegnerlukas.taskmanager.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

import java.util.Map;

public class TextAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		if (data.getType() == TaskAttributeType.TEXT) {
			TextAttributeData textData = (TextAttributeData) data;

			switch (var) {
				case TEXT_CHAR_LIMIT: {
					return setCharLimit(textData, newValue, changedVars);
				}
				case TEXT_MULTILINE: {
					return setMultiline(textData, newValue, changedVars);
				}
				case TEXT_N_LINES_EXP: {
					return setNLinesExpected(textData, newValue, changedVars);
				}
				case USE_DEFAULT: {
					return setUseDefault(textData, newValue, changedVars);
				}
				case DEFAULT_VALUE: {
					return setDefaultValue(textData, newValue, changedVars);
				}
				default: {
					return false;
				}
			}

		} else {
			return false;
		}

	}




	private boolean setCharLimit(TextAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.charLimit = ((NumberValue) value).getInt();
			changedVars.put(TaskAttributeData.Var.TEXT_CHAR_LIMIT, value);
			if (data.defaultValue.length() > data.charLimit) {
				setDefaultValue(data, new TextValue(data.defaultValue.substring(0, data.charLimit)), changedVars);
			}
			return true;
		} else {
			return false;
		}
	}




	private boolean setMultiline(TextAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.multiline = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.TEXT_MULTILINE, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setNLinesExpected(TextAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof NumberValue) {
			data.nLinesExpected = ((NumberValue) value).getInt();
			changedVars.put(TaskAttributeData.Var.TEXT_N_LINES_EXP, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setUseDefault(TextAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.useDefault = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.USE_DEFAULT, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setDefaultValue(TextAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof TextValue) {
			data.defaultValue = ((TextValue) value).getText();
			changedVars.put(TaskAttributeData.Var.DEFAULT_VALUE, value);
			return true;
		} else {
			return false;
		}
	}


}
