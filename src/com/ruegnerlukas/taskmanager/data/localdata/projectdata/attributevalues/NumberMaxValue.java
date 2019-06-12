package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

public class NumberMaxValue extends AttributeValue<Double> {


	public NumberMaxValue(double max) {
		super(max, AttributeValueType.NUMBER_MAX);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Double.compare(this.getValue(), ((NumberMaxValue) other).getValue());
			}
		}
	}

}
