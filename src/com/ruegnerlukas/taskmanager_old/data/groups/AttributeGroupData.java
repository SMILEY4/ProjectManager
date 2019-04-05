package com.ruegnerlukas.taskmanager_old.data.groups;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class AttributeGroupData {


	public String name;
	public boolean useCustomHeader;
	public String customHeader;
	public List<TaskAttribute> attributes;




	public AttributeGroupData() {
		this("", false, "", new ArrayList<>());
	}




	public AttributeGroupData(String name, boolean useCustomHeader, String customHeader, List<TaskAttribute> attributes) {
		this.name = name;
		this.useCustomHeader = useCustomHeader;
		this.customHeader = customHeader;
		this.attributes = attributes;
	}


}
