package com.ruegnerlukas.taskmanager.logic_v1.data.taskattribs;

import com.ruegnerlukas.taskmanager.logic_v1.data.taskattribs.CustomAttribute.Type;

public class CustomNumberAttributeData implements CustomAttributeData{

	public int decPlaces = 0;
	public double min = -100;
	public double max = +100;
	public boolean useDefault = false;
	public double defaultValue = 0;
	
	@Override
	public Type getType() {
		return Type.NUMBER;
	}

}
