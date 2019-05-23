package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

public abstract class AttributeValue<T> {


	private final T value;
	private final AttributeValueType type;




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
		if(other instanceof AttributeValue) {
			return this.compare((AttributeValue<?>)other) == 0;
		} else {
			return false;
		}
	}



	public abstract int compare(AttributeValue<?> other);

}
