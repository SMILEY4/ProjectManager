package com.ruegnerlukas.taskmanager.logic.data.taskattribs;

import java.time.LocalDate;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;

public class CustomDateAttributeData implements CustomAttributeData{

	public boolean useDefault = false;
	public LocalDate defaultValue = LocalDate.now();
	
	@Override
	public Type getType() {
		return Type.DATE;
	}

}
