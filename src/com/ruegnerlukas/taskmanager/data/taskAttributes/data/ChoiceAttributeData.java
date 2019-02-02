package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChoiceAttributeData implements TaskAttributeData {


	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		Map<Var, TaskAttributeValue> changedVars = new HashMap<>();

		switch (var) {

			case CHOICE_ATT_VALUES: {
				if (newValue instanceof TextArrayValue) {

					this.values = ((TextArrayValue) newValue).getText();
					changedVars.put(Var.CHOICE_ATT_VALUES, newValue);

					boolean foundDefault = false;
					for (String value : values) {
						if (value.equals(defaultValue)) {
							foundDefault = true;
							break;
						}
					}
					if (!foundDefault) {
						this.defaultValue = values.length == 0 ? "" : values[0];
						changedVars.put(Var.DEFAULT_VALUE, new TextValue(defaultValue));
					}

				}
			}

			case USE_DEFAULT: {
				if (newValue instanceof BoolValue) {
					useDefault = ((BoolValue) newValue).getBoolValue();
					changedVars.put(Var.USE_DEFAULT, newValue);
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if (newValue instanceof TextValue) {
					defaultValue = ((TextValue) newValue).getText();
					changedVars.put(Var.DEFAULT_VALUE, newValue);
				} else {
					return null;
				}
			}

		}

		return changedVars;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !useDefault;
		} else {
			if (value instanceof TextValue) {
				return new HashSet<>(Arrays.asList(values)).contains(((TextValue) value).getText());
			} else {
				return false;
			}
		}
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public TextValue getDefault() {
		return new TextValue(defaultValue);
	}

}
