package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

public class UseDefaultValue extends AttributeValue<Boolean> {





	public UseDefaultValue(boolean useDefault) {
		super(useDefault, AttributeValueType.USE_DEFAULT);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return Boolean.compare(this.getValue(), ((UseDefaultValue) other).getValue());
			}
		}
	}

}
