package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.util.HashMap;

public class POJOTaskAttribute {


	public int id;
	public String name;
	public AttributeType type;
	public HashMap<AttributeValueType, AttributeValue<?>> values;




	public POJOTaskAttribute(TaskAttribute attribute) {
		this.id = attribute.id;
		this.name = attribute.name.get();
		this.type = attribute.type.get();
		this.values = new HashMap<>();
		this.values.putAll(attribute.values);
	}




	public TaskAttribute toAttribute(Project project) {
		TaskAttribute attribute = new TaskAttribute(id, type, project, project.dataHandler);
		AttributeLogic.initTaskAttribute(attribute);
		attribute.name.set(name);
		attribute.values.putAll(values);
		return attribute;
	}

}
