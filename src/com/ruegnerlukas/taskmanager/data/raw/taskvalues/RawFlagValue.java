package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.raw.RawFlag;

public class RawFlagValue extends RawTaskValue {


	public RawFlag value;




	public static RawFlagValue toRaw(FlagValue value) {
		RawFlagValue raw = new RawFlagValue();
		raw.type = AttributeType.FLAG;
		raw.value = RawFlag.toRaw(value.getValue());
		return raw;
	}

}
