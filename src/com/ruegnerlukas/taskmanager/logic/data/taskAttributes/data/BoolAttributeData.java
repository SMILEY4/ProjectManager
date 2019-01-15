package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

public class BoolAttributeData implements TaskAttributeData {

	public boolean useDefault = false;
	public boolean defaultValue = false;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.BOOLEAN;
	}




	@Override
	public boolean update(Var var, Object newValue) {

		switch (var) {

			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return true;
				} else {
					return false;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof Boolean) {
					defaultValue = (Boolean)newValue;
					return true;
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}
	}





}
