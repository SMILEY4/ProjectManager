package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NumberValue;

public class RawNumberValue extends RawTaskValue {


	public double value;




	public static RawNumberValue toRaw(NumberValue value) {
		RawNumberValue raw = new RawNumberValue();
		raw.type = AttributeType.NUMBER;
		raw.value = value.getValue();
		return raw;
	}




	public static NumberValue fromRaw(RawNumberValue rawValue) {
		return new NumberValue(rawValue.value);
	}

}
