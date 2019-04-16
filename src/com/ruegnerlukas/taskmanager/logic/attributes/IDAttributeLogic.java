package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class IDAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Integer.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static Comparator<Integer> ID_COMPARATOR = Integer::compareTo;




	public static Comparator getComparator() {
		return ID_COMPARATOR;
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("IDAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.ID);
		IDAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
	}


}
