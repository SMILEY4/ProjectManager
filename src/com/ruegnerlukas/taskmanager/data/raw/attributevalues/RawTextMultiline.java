package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.TextMultilineValue;

public class RawTextMultiline extends RawAttributeValue {


	public boolean value;




	public static RawTextMultiline toRaw(TextMultilineValue value) {
		RawTextMultiline raw = new RawTextMultiline();
		raw.type = AttributeValueType.TEXT_MULTILINE;
		raw.value = value.getValue();
		return raw;
	}

}
