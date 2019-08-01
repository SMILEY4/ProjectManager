package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.ChoiceValue;

public class RawChoiceValue extends RawTaskValue {


	public String value;




	public static RawChoiceValue toRaw(ChoiceValue value) {
		RawChoiceValue raw = new RawChoiceValue();
		raw.type = AttributeType.CHOICE;
		raw.value = value.getValue();
		return raw;
	}




	public static ChoiceValue fromRaw(RawChoiceValue rawValue) {
		return new ChoiceValue(rawValue.value);
	}

}
