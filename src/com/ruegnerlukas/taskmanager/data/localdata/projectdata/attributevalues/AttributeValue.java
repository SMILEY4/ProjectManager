package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

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

}
