package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

public class TextMultilineValue extends AttributeValue<Boolean> {


	public TextMultilineValue(boolean multiline) {
		super(multiline, AttributeValueType.TEXT_MULTILINE);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return this.getValue().compareTo(((TextMultilineValue) other).getValue());
			}
		}
	}


}
