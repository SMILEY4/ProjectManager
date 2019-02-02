package com.ruegnerlukas.taskmanager.data.taskAttributes.data;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.HashMap;
import java.util.Map;

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
	public Map<Var, TaskAttributeValue> update(Var var, TaskAttributeValue newValue) {
		Map<Var, TaskAttributeValue> changedVars = new HashMap<>();

		switch (var) {

			case NUMBER_ATT_DEC_PLACES: {
				if (newValue instanceof NumberValue) {
					decPlaces = ((NumberValue) newValue).getInt();
					defaultValue = MathUtils.setDecPlaces(defaultValue, decPlaces);
					min = MathUtils.setDecPlaces(min, decPlaces);
					max = MathUtils.setDecPlaces(max, decPlaces);
					changedVars.put(Var.NUMBER_ATT_DEC_PLACES, newValue);
					changedVars.put(Var.DEFAULT_VALUE, new NumberValue(defaultValue));
					changedVars.put(Var.NUMBER_ATT_MIN, new NumberValue(min));
					changedVars.put(Var.NUMBER_ATT_MAX, new NumberValue(max));
				}
			}

			case NUMBER_ATT_MIN: {
				if (newValue instanceof NumberValue) {
					min = ((NumberValue) newValue).getDouble();
					changedVars.put(Var.NUMBER_ATT_MIN, newValue);
				}
			}

			case NUMBER_ATT_MAX: {
				if (newValue instanceof NumberValue) {
					max = ((NumberValue) newValue).getDouble();
					changedVars.put(Var.NUMBER_ATT_MAX, newValue);
				}
			}

			case USE_DEFAULT: {
				if (newValue instanceof BoolValue) {
					useDefault = ((BoolValue) newValue).getBoolValue();
					changedVars.put(Var.USE_DEFAULT, newValue);
				}
			}

			case DEFAULT_VALUE: {
				if (newValue instanceof NumberValue) {
					defaultValue = ((NumberValue) newValue).getDouble();
					changedVars.put(Var.DEFAULT_VALUE, newValue);
				}
			}

		}

		return changedVars;
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if (value instanceof NoValue) {
			return !useDefault;
		} else {
			if (value instanceof NumberValue) {
				final double number = ((NumberValue) value).getDouble();
				return min <= number && number <= max;
			} else {
				return false;
			}
		}
	}




	@Override
	public boolean usesDefault() {
		return useDefault;
	}




	@Override
	public NumberValue getDefault() {
		return new NumberValue(defaultValue);
	}

}
