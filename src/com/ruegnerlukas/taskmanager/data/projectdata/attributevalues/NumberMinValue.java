package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

public class NumberMinValue extends AttributeValue<Double> {


	public NumberMinValue(double min) {
		super(min, AttributeValueType.NUMBER_MIN);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Double.compare(this.getValue(), ((NumberMinValue) other).getValue());
			}
		}
	}

}
