package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.NumberDecPlacesValue;

public class RawNumberDecPlaces extends RawAttributeValue {


	public int value;




	public static RawNumberDecPlaces toRaw(NumberDecPlacesValue value) {
		RawNumberDecPlaces raw = new RawNumberDecPlaces();
		raw.type = AttributeValueType.NUMBER_DEC_PLACES;
		raw.value = value.getValue();
		return raw;
	}




	public static NumberDecPlacesValue fromRaw(RawNumberDecPlaces rawValue) {
		return new NumberDecPlacesValue(rawValue.value);
	}

}
