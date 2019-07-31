package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

public class RawDependencyValue extends RawTaskValue {


	public int[] values;




	public static RawDependencyValue toRaw(DependencyValue value) {
		RawDependencyValue raw = new RawDependencyValue();
		raw.type = AttributeType.DEPENDENCY;
		raw.values = new int[value.getValue().length];
		for (int i = 0; i < raw.values.length; i++) {
			raw.values[i] = TaskLogic.getTaskID(value.getValue()[i]);
		}
		return raw;
	}

}
