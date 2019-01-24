package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TextValue;

import java.util.Arrays;
import java.util.HashSet;

public class ChoiceAttributeData implements TaskAttributeData {

	public String[] values = new String[0];
	public boolean useDefault = false;
	public String defaultValue = "";




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.CHOICE;
	}




	@Override
	public Var[] update(Var var, TaskAttributeValue newValue) {

		switch (var) {

			case CHOICE_ATT_VALUES: {
				if(newValue instanceof TextArrayValue) {
					values = ((TextArrayValue)newValue).getText();

					boolean foundDefault = false;
					for(String value : values) {
						if(value.equals(defaultValue)) {
							foundDefault = true;
							break;
						}
					}
					if(!foundDefault) {
						this.defaultValue = values.length==0 ? "" : values[0];
						return new Var[] {Var.CHOICE_ATT_VALUES, Var.DEFAULT_VALUE};
					} else {
						return new Var[] {Var.CHOICE_ATT_VALUES};
					}

				} else {
					return null;
				}
			}

			case USE_DEFAULT: {
				if(newValue instanceof BoolValue) {
					useDefault = ((BoolValue)newValue).getBoolValue();
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof TextValue) {
					defaultValue = ((TextValue)newValue).getText();
					return new Var[] {Var.DEFAULT_VALUE};
				} else {
					return null;
				}
			}

			default: {
				return null;
			}
		}
	}


	@Override
	public boolean validate(TaskAttributeValue value) {
		if(value instanceof TextValue) {
			return new HashSet<>(Arrays.asList(values)).contains( ((TextValue)value).getText() );
		} else {
			return false;
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
