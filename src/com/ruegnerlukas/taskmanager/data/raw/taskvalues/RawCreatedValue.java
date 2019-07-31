package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.CreatedValue;

import java.time.LocalDateTime;

public class RawCreatedValue extends RawTaskValue {


	public LocalDateTime value;




	public static RawCreatedValue toRaw(CreatedValue value) {
		RawCreatedValue raw = new RawCreatedValue();
		raw.type = AttributeType.CREATED;
		raw.value = value.getValue();
		return raw;
	}

}
