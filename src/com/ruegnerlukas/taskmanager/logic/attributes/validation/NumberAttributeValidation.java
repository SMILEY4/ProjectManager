package com.ruegnerlukas.taskmanager.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class NumberAttributeValidation extends AttributeValidator {


	@Override
	public boolean validate(TaskAttributeData data, TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !data.usesDefault();
		} else {
			if (value instanceof NumberValue) {
				NumberAttributeData numberData = (NumberAttributeData) data;
				final double number = ((NumberValue) value).getDouble();
				return numberData.min <= number && number <= numberData.max;
			} else {
				return false;
			}
		}
	}

}
