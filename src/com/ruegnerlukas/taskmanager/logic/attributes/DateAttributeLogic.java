package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DateAttributeLogic {


	public static final String DATE_USE_DEFAULT = "date_use_default";
	public static final String DATE_DEFAULT_VALUE = "date_default_value";


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(DATE_USE_DEFAULT, Boolean.class);
		map.put(DATE_DEFAULT_VALUE, LocalDate.class);
		map.put("task_value", LocalDate.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}



	public static TaskAttribute createAttribute() {
		return createAttribute("DateAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DATE);
		DateAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, LocalDate.now());
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(DATE_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(DATE_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, LocalDate defaultValue) {
		attribute.values.put(DATE_DEFAULT_VALUE, defaultValue);
	}




	public static LocalDate getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(DATE_DEFAULT_VALUE, LocalDate.class);
	}


}
