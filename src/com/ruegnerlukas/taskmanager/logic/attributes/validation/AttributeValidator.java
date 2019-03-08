package com.ruegnerlukas.taskmanager.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public abstract class AttributeValidator {

	public abstract boolean validate(TaskAttributeData data, TaskAttributeValue value);

}
