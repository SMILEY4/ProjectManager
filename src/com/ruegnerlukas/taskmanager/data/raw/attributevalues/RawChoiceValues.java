package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.ChoiceListValue;

public class RawChoiceValues extends RawAttributeValue {


	public String[] values;




	public static RawChoiceValues toRaw(ChoiceListValue value) {
		RawChoiceValues raw = new RawChoiceValues();
		raw.type = AttributeValueType.CHOICE_VALUES;
		raw.values = value.getValue();
		return raw;
	}




	public static ChoiceListValue fromRaw(RawChoiceValues rawValue) {
		return new ChoiceListValue(rawValue.values);
	}

}
