package com.ruegnerlukas.taskmanager.logic.data.taskattribs;

import java.util.ArrayList;
import java.util.List;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;

public class CustomChoiceAttributeData implements CustomAttributeData{

	public List<String> values = new ArrayList<String>();
	public boolean useDefault = false;
	public String defaultValue = "";
	
	@Override
	public Type getType() {
		return Type.CHOICE;
	}

}
