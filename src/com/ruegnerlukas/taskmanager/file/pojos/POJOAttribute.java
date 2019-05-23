package com.ruegnerlukas.taskmanager.file.pojos;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;

import java.util.Map;

public class POJOAttribute {


	public static POJOAttribute fromAttribute(TaskAttribute attribute) {
		POJOAttribute pojo = new POJOAttribute();
		pojo.name = attribute.name.get();
		pojo.type = attribute.type.get();
//		pojo.values = new HashMap<>(attribute.values);
		return pojo;
	}




	public static TaskAttribute toAttribute(POJOAttribute pojo) {
		TaskAttribute attribute = new TaskAttribute(pojo.name, pojo.type);
//		attribute.values.putAll(pojo.values);
		return attribute;
	}




	public String name;
	public AttributeType type;
	public Map<String, Object> values;

}
