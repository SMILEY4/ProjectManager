package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

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
	public Var[] update(Var var, TaskAttributeValue newValue) {

		switch (var) {

			case NUMBER_ATT_DEC_PLACES: {
				if(newValue instanceof NumberValue) {
					decPlaces = ((NumberValue)newValue).getInt();
					defaultValue = MathUtils.setDecPlaces(defaultValue, decPlaces);
					min = MathUtils.setDecPlaces(min, decPlaces);
					max = MathUtils.setDecPlaces(max, decPlaces);
					return new Var[] {
							Var.NUMBER_ATT_DEC_PLACES,
							Var.DEFAULT_VALUE,
							Var.NUMBER_ATT_MIN,
							Var.NUMBER_ATT_MAX };
				} else {
					return null;
				}
			}

			case NUMBER_ATT_MIN: {
				if(newValue instanceof NumberValue) {
					min = ((NumberValue)newValue).getDouble();
					return new Var[] {Var.NUMBER_ATT_MIN};
				} else {
					return null;
				}
			}

			case NUMBER_ATT_MAX: {
				if(newValue instanceof NumberValue) {
					max = ((NumberValue)newValue).getDouble();
					return new Var[] {Var.NUMBER_ATT_MAX};
				} else {
					return null;
				}
			}

			case USE_DEFAULT: {
				if(newValue instanceof BoolValue) {
					useDefault = ((BoolValue)newValue).getBoolValue();
					return new Var[] {Var.USE_DEFAULT};
				} else {
					return null;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof NumberValue) {
					defaultValue = ((NumberValue)newValue).getDouble();
					return new Var[] {Var.DEFAULT_VALUE};
				} else {
					return null;
				}
			}

			default: {
				return null;
			}
		}
	}




	@Override
	public boolean validate(TaskAttributeValue value) {
		if(value instanceof NoValue) {
			return !useDefault;
		} else {
			if(value instanceof NumberValue) {
				final double number = ((NumberValue)value).getDouble();
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
