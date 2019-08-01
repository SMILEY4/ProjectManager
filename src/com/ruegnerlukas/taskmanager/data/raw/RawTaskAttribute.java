package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.raw.attributevalues.RawAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.util.HashMap;
import java.util.Map;


public class RawTaskAttribute {


	public int id;
	public String name;
	public AttributeType type;
	public HashMap<AttributeValueType, RawAttributeValue> values = new HashMap<>();




	public static RawTaskAttribute toRaw(TaskAttribute attribute) {
		RawTaskAttribute raw = new RawTaskAttribute();
		raw.id = attribute.id;
		raw.name = attribute.name.get();
		raw.type = attribute.type.get();
		for (AttributeValueType type : attribute.values.keySet()) {
			raw.values.put(type, RawAttributeValue.toRaw(attribute.values.get(type)));
		}
		return raw;
	}




	public static TaskAttribute fromRaw(RawTaskAttribute rawAttribute, RawProject rawProject, Project project) {
		TaskAttribute attribute = baseFromRaw(rawAttribute, project);
		fromRaw(attribute, rawAttribute, rawProject, project);
		return attribute;
	}




	public static TaskAttribute baseFromRaw(RawTaskAttribute rawAttribute, Project project) {
		TaskAttribute attribute = new TaskAttribute(rawAttribute.id, rawAttribute.type, project);
//		AttributeLogic.initTaskAttribute(attribute);
		attribute.name.set(rawAttribute.name);
		return attribute;
	}




	public static void fromRaw(TaskAttribute attribute, RawTaskAttribute rawAttribute, RawProject rawProject, Project project) {
		for (Map.Entry<AttributeValueType, RawAttributeValue> entry : rawAttribute.values.entrySet()) {
			if(!attribute.values.containsKey(entry.getKey())) {
				attribute.values.put(entry.getKey(), RawAttributeValue.fromRaw(entry.getValue(), rawProject, project));
			}
		}
	}


}
