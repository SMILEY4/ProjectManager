package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.LastUpdatedValue;

import java.time.LocalDateTime;

public class RawLastUpdatedValue extends RawTaskValue {


	public LocalDateTime value;




	public static RawLastUpdatedValue toRaw(LastUpdatedValue value) {
		RawLastUpdatedValue raw = new RawLastUpdatedValue();
		raw.type = AttributeType.LAST_UPDATED;
		raw.value = value.getValue();
		return raw;
	}

}
