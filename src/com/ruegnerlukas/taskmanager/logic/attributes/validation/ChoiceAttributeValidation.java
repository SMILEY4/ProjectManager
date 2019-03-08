package com.ruegnerlukas.taskmanager.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

import java.util.Arrays;
import java.util.HashSet;

public class ChoiceAttributeValidation extends AttributeValidator {


	@Override
	public boolean validate(TaskAttributeData data, TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !data.usesDefault();
		} else {
			if (value instanceof TextValue) {
				ChoiceAttributeData choiceData = (ChoiceAttributeData) data;
				return new HashSet<>(Arrays.asList(choiceData.values)).contains(((TextValue) value).getText());
			} else {
				return false;
			}
		}
	}

}
