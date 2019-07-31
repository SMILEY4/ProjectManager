package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DescriptionValue;

public class RawDescriptionValue extends RawTaskValue {


	public String value;




	public static RawDescriptionValue toRaw(DescriptionValue value) {
		RawDescriptionValue raw = new RawDescriptionValue();
		raw.type = AttributeType.DESCRIPTION;
		raw.value = value.getValue();
		return raw;
	}

}
