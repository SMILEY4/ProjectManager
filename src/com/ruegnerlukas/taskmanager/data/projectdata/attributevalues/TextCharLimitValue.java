package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

public class TextCharLimitValue extends AttributeValue<Integer> {


	public TextCharLimitValue(int charLimit) {
		super(charLimit, AttributeValueType.TEXT_CHAR_LIMIT);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Integer.compare(this.getValue(), ((TextCharLimitValue) other).getValue());
			}
		}
	}

}
