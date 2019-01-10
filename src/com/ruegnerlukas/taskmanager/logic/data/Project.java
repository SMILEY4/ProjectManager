package com.ruegnerlukas.taskmanager.logic.data;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;

import java.util.ArrayList;
import java.util.List;

public class Project {
	
	public String name;

	public boolean attributesLocked = false;
	public List<TaskAttribute> attributes = new ArrayList<>();


	public Project(String name) {
		this.name = name;
		attributes.add(new TaskAttribute("ID Attribute", TaskAttributeType.ID));
		attributes.add(new TaskAttribute("Flag Attribute", TaskAttributeType.FLAG));
		attributes.add(new TaskAttribute("Description Attribute", TaskAttributeType.DESCRIPTION));
	}
	
}
