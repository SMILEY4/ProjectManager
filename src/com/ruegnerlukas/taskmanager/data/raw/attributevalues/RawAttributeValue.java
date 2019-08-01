package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.raw.RawProject;

public class RawAttributeValue {


	public AttributeValueType type;




	public static RawAttributeValue toRaw(AttributeValue<?> value) {

		switch (value.getType()) {
			case USE_DEFAULT: {
				return RawUseDefault.toRaw((UseDefaultValue) value);
			}
			case DEFAULT_VALUE: {
				return RawDefaultValue.toRaw((DefaultValue) value);
			}
			case CARD_DISPLAY_TYPE: {
				return RawCardDisplayType.toRaw((CardDisplayTypeValue) value);
			}
			case NUMBER_DEC_PLACES: {
				return RawNumberDecPlaces.toRaw((NumberDecPlacesValue) value);
			}
			case NUMBER_MIN: {
				return RawNumberMin.toRaw((NumberMinValue) value);
			}
			case NUMBER_MAX: {
				return RawNumberMax.toRaw((NumberMaxValue) value);
			}
			case CHOICE_VALUES: {
				return RawChoiceValues.toRaw((ChoiceListValue) value);
			}
			case FLAG_LIST: {
				return RawFlagList.toRaw((FlagListValue) value);
			}
			case TEXT_MULTILINE: {
				return RawTextMultiline.toRaw((TextMultilineValue) value);
			}
			default: {
				return null;
			}
		}

	}




	public static AttributeValue<?> fromRaw(RawAttributeValue rawValue, RawProject rawProject, Project project) {

		switch (rawValue.type) {
			case USE_DEFAULT: {
				return RawUseDefault.fromRaw((RawUseDefault) rawValue);
			}
			case DEFAULT_VALUE: {
				return RawDefaultValue.fromRaw((RawDefaultValue) rawValue, rawProject, project);
			}
			case CARD_DISPLAY_TYPE: {
				return RawCardDisplayType.fromRaw((RawCardDisplayType) rawValue);
			}
			case NUMBER_DEC_PLACES: {
				return RawNumberDecPlaces.fromRaw((RawNumberDecPlaces) rawValue);
			}
			case NUMBER_MIN: {
				return RawNumberMin.fromRaw((RawNumberMin) rawValue);
			}
			case NUMBER_MAX: {
				return RawNumberMax.fromRaw((RawNumberMax) rawValue);
			}
			case CHOICE_VALUES: {
				return RawChoiceValues.fromRaw((RawChoiceValues) rawValue);
			}
			case FLAG_LIST: {
				return RawFlagList.fromRaw((RawFlagList) rawValue);
			}
			case TEXT_MULTILINE: {
				return RawTextMultiline.fromRaw((RawTextMultiline) rawValue);
			}
			default: {
				return null;
			}
		}

	}

}
