package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.BoolValue;

public class RawBoolValue extends RawTaskValue {


	public boolean value;




	public static RawBoolValue toRaw(BoolValue value) {
		RawBoolValue raw = new RawBoolValue();
		raw.type = AttributeType.BOOLEAN;
		raw.value = value.getValue();
		return raw;
	}

}
