package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

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
	public boolean update(Var var, Object newValue) {

		switch (var) {

			case NUMBER_ATT_DEC_PLACES: {
				if(newValue instanceof Integer) {
					decPlaces = (Integer)newValue;
					defaultValue = MathUtils.setDecPlaces(defaultValue, decPlaces);
					min = MathUtils.setDecPlaces(min, decPlaces);
					max = MathUtils.setDecPlaces(max, decPlaces);
					return true;
				} else {
					return false;
				}
			}

			case NUMBER_ATT_MIN: {
				if(newValue instanceof Double) {
					min = (Double) newValue;
					return true;
				} else if(newValue instanceof Integer) {
					min = (Integer)newValue;
					return true;
				} else {
					return false;
				}
			}

			case NUMBER_ATT_MAX: {
				if(newValue instanceof Double) {
					max = (Double)newValue;
					return true;
				} else if(newValue instanceof Integer) {
					max = (Integer)newValue;
					return true;
				} else {
					return false;
				}
			}

			case USE_DEFAULT: {
				if(newValue instanceof Boolean) {
					useDefault = (Boolean)newValue;
					return true;
				} else {
					return false;
				}
			}

			case DEFAULT_VALUE: {
				if(newValue instanceof Double) {
					defaultValue = (Double)newValue;
					return true;
				} else if(newValue instanceof Integer) {
					defaultValue = (Integer)newValue;
					return true;
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}
	}
}
