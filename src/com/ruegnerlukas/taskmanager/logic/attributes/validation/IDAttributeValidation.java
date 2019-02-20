package com.ruegnerlukas.taskmanager.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class IDAttributeValidation extends AttributeValidator {


	@Override
	public boolean validate(TaskAttributeData data, TaskAttributeValue value) {
		if (value instanceof NumberValue) {
			return ((NumberValue) value).getInt() >= 0;
		} else {
			return false;
		}
	}

}
