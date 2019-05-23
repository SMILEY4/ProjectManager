package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

public class CardDisplayTypeValue extends AttributeValue<Boolean> {


	public CardDisplayTypeValue(boolean showOnCard) {
		super(showOnCard, AttributeValueType.CARD_DISPLAY_TYPE);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Boolean.compare(this.getValue(), ((CardDisplayTypeValue) other).getValue());
			}
		}
	}


}
