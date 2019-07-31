package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.raw.taskvalues.RawTaskValue;

public class RawDefaultValue extends RawAttributeValue {


	public RawTaskValue value;




	public static RawDefaultValue toRaw(DefaultValue value) {
		RawDefaultValue raw = new RawDefaultValue();
		raw.type = AttributeValueType.DEFAULT_VALUE;
		raw.value = RawTaskValue.toRaw(value.getValue());
		return raw;
	}


}
