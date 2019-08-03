package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

public abstract class AttributeValue<T> {


	private T value;
	private AttributeValueType type;




	protected AttributeValue(T value, AttributeValueType type) {
		this.value = value;
		this.type = type;
	}




	public AttributeValueType getType() {
		return type;
	}




	public T getValue() {
		return value;
	}




	@Override
	public boolean equals(Object other) {
		if (other instanceof AttributeValue) {
			return this.compare((AttributeValue<?>) other) == 0;
		} else {
			return false;
		}
	}




	public abstract int compare(AttributeValue<?> other);




	public static String valueToString(AttributeValue<?> attValue) {

		if (attValue == null || attValue.getValue() == null) {
			return "empty";
		}

		switch (attValue.getType()) {

			case USE_DEFAULT: {
				return ((UseDefaultValue) attValue).getValue() ? "True" : "False";
			}
			case DEFAULT_VALUE: {
				return TaskValue.valueToString(((DefaultValue) attValue).getValue());
			}
			case CARD_DISPLAY_TYPE: {
				return ((CardDisplayTypeValue) attValue).getValue() ? "Show" : "Hide";
			}
			case NUMBER_DEC_PLACES: {
				return ((NumberDecPlacesValue) attValue).getValue().toString();
			}
			case NUMBER_MIN: {
				return ((NumberMinValue) attValue).getValue().toString();
			}
			case NUMBER_MAX: {
				return ((NumberMaxValue) attValue).getValue().toString();
			}
			case CHOICE_VALUES: {
				return String.join(", ", ((ChoiceListValue) attValue).getValue());
			}
			case FLAG_LIST: {
				TaskFlag[] flags = ((FlagListValue) attValue).getValue();
				String[] names = new String[flags.length];
				for (int i = 0; i < flags.length; i++) {
					names[i] = flags[i].name.get();
				}
				return String.join(", ", names);
			}
			case TEXT_MULTILINE: {
				return ((TextMultilineValue) attValue).getValue() ? "Multiple Lines" : "Single Line";
			}
			default: {
				return "?unknowntype?";
			}
		}

	}

}
