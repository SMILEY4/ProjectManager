package com.ruegnerlukas.taskmanager.logic.data.taskattribs;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;

public class CustomBoolAttributeData implements CustomAttributeData{

	public boolean useDefault = false;
	public boolean defaultValue = false;
	
	@Override
	public Type getType() {
		return Type.BOOLEAN;
	}

}
