package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

public class NumberDecPlacesValue extends AttributeValue<Integer> {


	public NumberDecPlacesValue(int decPlaces) {
		super(decPlaces, AttributeValueType.NUMBER_DEC_PLACES);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Integer.compare(this.getValue(), ((NumberDecPlacesValue) other).getValue());
			}
		}
	}

}
