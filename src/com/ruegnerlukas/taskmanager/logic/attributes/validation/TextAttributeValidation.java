package com.ruegnerlukas.taskmanager.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class TextAttributeValidation extends AttributeValidator {


	@Override
	public boolean validate(TaskAttributeData data, TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !data.usesDefault();
		} else {
			TextAttributeData textData = (TextAttributeData) data;
			return value instanceof TextValue && (((TextValue) value).getText().length() <= textData.charLimit);
		}
	}

}
