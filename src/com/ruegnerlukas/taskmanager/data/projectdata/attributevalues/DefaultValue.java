package com.ruegnerlukas.taskmanager.data.projectdata.attributevalues;

import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;

public class DefaultValue extends AttributeValue<TaskValue<?>> {


	public DefaultValue(TaskValue<?> value) {
		super(value, AttributeValueType.DEFAULT_VALUE);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				return this.getValue().compare(((DefaultValue) other).getValue());
			}
		}
	}


}
