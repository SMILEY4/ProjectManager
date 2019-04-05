package com.ruegnerlukas.taskmanager_old.data.taskAttributes.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class NumberAttributeData implements TaskAttributeData {


	public int decPlaces = 0;
	public double min = -100;
	public double max = +100;
	public boolean useDefault = false;
	public double defaultValue = 0;




	@Override
	public TaskAttributeType getType() {
		return TaskAttributeType.NUMBER;
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public NumberValue getDefault() {
		return new NumberValue(defaultValue);
	}




	@Override
	public TaskAttributeValue getValue(Var var) {
		if (var == Var.NUMBER_ATT_DEC_PLACES) {
			return new NumberValue(decPlaces);
		}
		if (var == Var.NUMBER_ATT_MIN) {
			return new NumberValue(min);
		}
		if (var == Var.NUMBER_ATT_MAX) {
			return new NumberValue(max);
		}
		if (var == Var.USE_DEFAULT) {
			return new BoolValue(useDefault);
		}
		if (var == Var.DEFAULT_VALUE) {
			return getDefault();
		}
		return null;
	}




	@Override
	public NumberAttributeData copy() {
		NumberAttributeData copy = new NumberAttributeData();
		copy.decPlaces = this.decPlaces;
		copy.min = this.min;
		copy.max = this.max;
		copy.useDefault = this.useDefault;
		copy.defaultValue = this.defaultValue;
		return copy;
	}

}
