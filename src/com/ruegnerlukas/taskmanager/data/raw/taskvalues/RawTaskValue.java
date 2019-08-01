package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.data.raw.RawProject;

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




	public static TaskValue<?> fromRaw(RawTaskValue value, RawProject rawProject, Project project) {
		switch (value.type) {
			case DESCRIPTION: {
				return RawDescriptionValue.fromRaw((RawDescriptionValue) value);
			}
			case CREATED: {
				return RawCreatedValue.fromRaw((RawCreatedValue) value);
			}
			case LAST_UPDATED: {
				return RawLastUpdatedValue.fromRaw((RawLastUpdatedValue) value);
			}
			case FLAG: {
				return RawFlagValue.fromRaw((RawFlagValue) value, rawProject, project);
			}
			case TEXT: {
				return RawTextValue.fromRaw((RawTextValue) value);
			}
			case NUMBER: {
				return RawNumberValue.fromRaw((RawNumberValue) value);
			}
			case BOOLEAN: {
				return RawBoolValue.fromRaw((RawBoolValue) value);
			}
			case CHOICE: {
				return RawChoiceValue.fromRaw((RawChoiceValue) value);
			}
			case DATE: {
				return RawDateValue.fromRaw((RawDateValue) value);
			}
			case DEPENDENCY: {
				return RawDependencyValue.fromRaw((RawDependencyValue) value, rawProject, project);
			}
			default: {
				return null;
			}
		}
	}


}
