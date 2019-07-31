package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.CardDisplayTypeValue;

public class RawCardDisplayType extends RawAttributeValue {


	public boolean value;




	public static RawCardDisplayType toRaw(CardDisplayTypeValue value) {
		RawCardDisplayType raw = new RawCardDisplayType();
		raw.type = AttributeValueType.CARD_DISPLAY_TYPE;
		raw.value = value.getValue();
		return raw;
	}

}
