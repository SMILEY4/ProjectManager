package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

public class BoolAttributeData implements TaskAttributeData {

	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
	}




	@Override
	public Var[] update(Var var, TaskAttributeValue newValue) {

		switch (var) {

			case USE_DEFAULT: {
				if(newValue instanceof BoolValue) {
					useDefault = ((BoolValue)newValue).getBoolValue();
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof BoolValue) {
					defaultValue = ((BoolValue)newValue).getBoolValue();
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
		return (value instanceof BoolValue);
	}


}
