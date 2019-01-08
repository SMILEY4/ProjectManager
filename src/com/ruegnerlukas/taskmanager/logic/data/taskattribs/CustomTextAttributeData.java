package com.ruegnerlukas.taskmanager.logic.data.taskattribs;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;

public class CustomTextAttributeData implements CustomAttributeData{

	public int charLimit = 64;
	public boolean useDefault = false;
	public String defaultValue = "";
	
	@Override
	public Type getType() {
		return Type.TEXT;
	}

}
