package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;

public class RawTaskValue {


	public AttributeType type;




	public static RawTaskValue toRaw(TaskValue<?> value) {
		switch (value.getAttType()) {
			case DESCRIPTION: {
				return RawDescriptionValue.toRaw((DescriptionValue) value);
			}
			case CREATED: {
				return RawCreatedValue.toRaw((CreatedValue) value);
			}
			case LAST_UPDATED: {
				return RawLastUpdatedValue.toRaw((LastUpdatedValue) value);
			}
			case FLAG: {
				return RawFlagValue.toRaw((FlagValue) value);
			}
			case TEXT: {
				return RawTextValue.toRaw((TextValue) value);
			}
			case NUMBER: {
				return RawNumberValue.toRaw((NumberValue) value);
			}
			case BOOLEAN: {
				return RawBoolValue.toRaw((BoolValue) value);
			}
			case CHOICE: {
				return RawChoiceValue.toRaw((ChoiceValue) value);
			}
			case DATE: {
				return RawDateValue.toRaw((DateValue) value);
			}
			case DEPENDENCY: {
				return RawDependencyValue.toRaw((DependencyValue) value);
			}
			default: {
				return null;
			}
		}
	}


}
