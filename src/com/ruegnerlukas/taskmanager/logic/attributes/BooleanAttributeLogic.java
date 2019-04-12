package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BooleanAttributeLogic {


	public static final String BOOLEAN_USE_DEFAULT = "bool_use_default";
	public static final String BOOLEAN_DEFAULT_VALUE = "bool_default_value";


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put("task_value", Boolean.class);
		map.put(BOOLEAN_USE_DEFAULT, Boolean.class);
		map.put(BOOLEAN_DEFAULT_VALUE, Boolean.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("BooleanAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.BOOLEAN);
		BooleanAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, false);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(BOOLEAN_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(BOOLEAN_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, boolean defaultValue) {
		attribute.values.put(BOOLEAN_DEFAULT_VALUE, defaultValue);
	}




	public static boolean getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(BOOLEAN_DEFAULT_VALUE, Boolean.class);
	}


}
