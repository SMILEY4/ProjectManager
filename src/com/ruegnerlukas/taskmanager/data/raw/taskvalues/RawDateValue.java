package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DateValue;

import java.time.LocalDate;

public class RawDateValue extends RawTaskValue {


	public LocalDate value;




	public static RawDateValue toRaw(DateValue value) {
		RawDateValue raw = new RawDateValue();
		raw.type = AttributeType.DATE;
		raw.value = value.getValue();
		return raw;
	}




	public static DateValue fromRaw(RawDateValue rawValue) {
		return new DateValue(rawValue.value);
	}

}
