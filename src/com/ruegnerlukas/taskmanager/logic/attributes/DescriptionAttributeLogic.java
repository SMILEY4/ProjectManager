package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DescriptionAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, String.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, String.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static Comparator<String> DESCRIPTION_COMPARATOR = String::compareTo;




	public static Comparator getComparator() {
		return DESCRIPTION_COMPARATOR;
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("DescriptionAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DESCRIPTION);
		DescriptionAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, true);
		setDefaultValue(attribute, "");
	}




	private static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	private static void setDefaultValue(TaskAttribute attribute, String defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static String getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, String.class);
	}


}