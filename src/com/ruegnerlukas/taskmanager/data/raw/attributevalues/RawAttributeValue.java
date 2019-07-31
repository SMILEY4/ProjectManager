package com.ruegnerlukas.taskmanager.data.raw.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;

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


}
