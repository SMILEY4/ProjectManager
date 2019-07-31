package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.raw.attributevalues.RawAttributeValue;

import java.util.HashMap;


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
		for(AttributeValueType type : attribute.values.keySet()) {
			raw.values.put(type, RawAttributeValue.toRaw(attribute.values.get(type)));
		}
		return raw;
	}


}
