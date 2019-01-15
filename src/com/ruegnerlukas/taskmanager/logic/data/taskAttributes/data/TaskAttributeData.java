package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;


import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

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

	public boolean update(Var var, Object newValue);


}
