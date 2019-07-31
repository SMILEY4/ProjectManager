package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TextValue;

public class RawTextValue extends RawTaskValue {


	public String value;




	public static RawTextValue toRaw(TextValue value) {
		RawTextValue raw = new RawTextValue();
		raw.type = AttributeType.TEXT;
		raw.value = value.getValue();
		return raw;
	}

}
