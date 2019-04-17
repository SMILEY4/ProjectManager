package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;


import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public interface TaskAttributeData {


	enum Var {
		USE_DEFAULT,
		DEFAULT_VALUE,

		CHOICE_ATT_VALUES,

		FLAG_ATT_FLAGS,

		NUMBER_ATT_DEC_PLACES,
		NUMBER_ATT_MIN,
		NUMBER_ATT_MAX,

		TEXT_CHAR_LIMIT,
		TEXT_MULTILINE,
		TEXT_N_LINES_EXP
	}


	TaskAttributeType getType();

	boolean usesDefault();

	TaskAttributeValue getDefault();

	TaskAttributeValue getValue(Var var);

	TaskAttributeData copy();
}