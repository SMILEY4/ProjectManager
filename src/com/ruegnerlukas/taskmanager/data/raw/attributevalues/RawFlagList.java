package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.FlagListValue;
import com.ruegnerlukas.taskmanager.data.raw.RawFlag;

public class RawFlagList extends RawAttributeValue {


	public RawFlag[] values;




	public static RawFlagList toRaw(FlagListValue value) {
		RawFlagList raw = new RawFlagList();
		raw.type = AttributeValueType.FLAG_LIST;
		raw.values = new RawFlag[value.getValue().length];
		for (int i = 0; i < raw.values.length; i++) {
			raw.values[i] = RawFlag.toRaw(value.getValue()[i]);
		}
		return raw;
	}

}
