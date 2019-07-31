package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.NumberMinValue;

public class RawNumberMin extends RawAttributeValue {


	public double value;




	public static RawNumberMin toRaw(NumberMinValue value) {
		RawNumberMin raw = new RawNumberMin();
		raw.type = AttributeValueType.NUMBER_MIN;
		raw.value = value.getValue();
		return raw;
	}

}
