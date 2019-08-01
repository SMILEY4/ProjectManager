package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.NumberMaxValue;

public class RawNumberMax extends RawAttributeValue {


	public double value;




	public static RawNumberMax toRaw(NumberMaxValue value) {
		RawNumberMax raw = new RawNumberMax();
		raw.type = AttributeValueType.NUMBER_MAX;
		raw.value = value.getValue();
		return raw;
	}




	public static NumberMaxValue fromRaw(RawNumberMax rawValue) {
		return new NumberMaxValue(rawValue.value);
	}

}
