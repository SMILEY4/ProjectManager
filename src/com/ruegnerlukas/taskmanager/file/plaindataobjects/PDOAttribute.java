package com.ruegnerlukas.taskmanager.file.plaindataobjects;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;

import java.util.HashMap;
import java.util.Map;

public class PDOAttribute {


	public static PDOAttribute fromAttribute(TaskAttribute attribute) {
		PDOAttribute pdo = new PDOAttribute();
		pdo.name = attribute.name.get();
		pdo.type = attribute.type.get();
		pdo.values = new HashMap<>(attribute.values);
		return pdo;
	}




	public static TaskAttribute toAttribute(PDOAttribute pdo) {
		TaskAttribute attribute = new TaskAttribute(pdo.name, pdo.type);
		attribute.values.putAll(pdo.values);
		return attribute;
	}




	public String name;
	public AttributeType type;
	public Map<AttributeValueType, AttributeValue<?>> values;

}
