package com.ruegnerlukas.taskmanager_old.logic.attributes.validation;

import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.validation.AttributeValidator;

import java.util.Arrays;
import java.util.HashSet;

public class FlagAttributeValidation extends AttributeValidator {


	@Override
	public boolean validate(TaskAttributeData data, TaskAttributeValue value) {
		if (value instanceof FlagValue) {
			FlagAttributeData flagData = (FlagAttributeData) data;
			return new HashSet<>(Arrays.asList(flagData.flags)).contains(((FlagValue) value).getFlag());
		} else {
			return false;
		}
	}

}
