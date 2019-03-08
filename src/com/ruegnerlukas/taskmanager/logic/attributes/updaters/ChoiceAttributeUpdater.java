package com.ruegnerlukas.taskmanager.logic.attributes.updaters;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

import java.util.Map;

public class ChoiceAttributeUpdater extends AttributeUpdater {


	@Override
	public boolean update(TaskAttributeData data, TaskAttributeData.Var var, TaskAttributeValue newValue, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		if (data.getType() == TaskAttributeType.CHOICE) {
			ChoiceAttributeData choiceData = (ChoiceAttributeData) data;

			switch (var) {

				case CHOICE_ATT_VALUES: {
					return setChoiceValues(choiceData, newValue, changedVars);
				}
				case USE_DEFAULT: {
					return setUseDefault(choiceData, newValue, changedVars);
				}
				case DEFAULT_VALUE: {
					return setDefaultValue(choiceData, newValue, changedVars);
				}
				default: {
					return false;
				}
			}

		} else {
			return false;
		}

	}




	private boolean setChoiceValues(ChoiceAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof TextArrayValue) {

			String[] choices = ((TextArrayValue) value).getText();
			data.values = choices;
			changedVars.put(TaskAttributeData.Var.CHOICE_ATT_VALUES, value);

			boolean containsCurrentDefault = false;
			for (String choice : choices) {
				if (choice.equals(data.defaultValue)) {
					containsCurrentDefault = true;
					break;
				}
			}

			if (!containsCurrentDefault) {
				setDefaultValue(data, new TextValue(choices.length == 0 ? "" : choices[0]), changedVars);
			}

			return true;

		} else {
			return false;
		}
	}




	private boolean setUseDefault(ChoiceAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof BoolValue) {
			data.useDefault = ((BoolValue) value).getBoolValue();
			changedVars.put(TaskAttributeData.Var.USE_DEFAULT, value);
			return true;
		} else {
			return false;
		}
	}




	private boolean setDefaultValue(ChoiceAttributeData data, TaskAttributeValue value, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {
		if (value instanceof TextValue) {
			data.defaultValue = ((TextValue) value).getText();
			changedVars.put(TaskAttributeData.Var.DEFAULT_VALUE, value);
			return true;
		} else {
			return false;
		}
	}


}
