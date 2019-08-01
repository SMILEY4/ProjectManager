package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;

public class RawUseDefault extends RawAttributeValue {


	public boolean value;




	public static RawUseDefault toRaw(UseDefaultValue value) {
		RawUseDefault raw = new RawUseDefault();
		raw.type = AttributeValueType.USE_DEFAULT;
		raw.value = value.getValue();
		return raw;
	}




	public static UseDefaultValue fromRaw(RawUseDefault rawValue) {
		return new UseDefaultValue(rawValue.value);
	}

}
