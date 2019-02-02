package com.ruegnerlukas.taskmanager.data.taskAttributes.data;


import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Map;

public interface TaskAttributeData {


	public enum Var {
		USE_DEFAULT,
		DEFAULT_VALUE,

		CHOICE_ATT_VALUES,

		FLAG_ATT_FLAGS,

		NUMBER_ATT_DEC_PLACES,
		NUMBER_ATT_MIN,
		NUMBER_ATT_MAX,

		TEXT_CHAR_LIMIT,
		TEXT_MULTILINE
	}



	public TaskAttributeType getType();

	public Map<Var,TaskAttributeValue> update(Var var, TaskAttributeValue newValue);

	public boolean validate(TaskAttributeValue value);

	public boolean usesDefault();

	public TaskAttributeValue getDefault();

}
