package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TextAttributeLogic {


	public static final String TEXT_CHAR_LIMIT = "text_char_limit";
	public static final String TEXT_MULTILINE = "text_multiline";


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(TEXT_CHAR_LIMIT, Integer.class);
		map.put(TEXT_MULTILINE, Boolean.class);
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, String.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, String.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<String> COMPARATOR_ASC = String::compareTo;
	public static final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	public static TaskAttribute createAttribute() {
		return createAttribute("TextAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.TEXT);
		TextAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setCharLimit(attribute, 100);
		setMultiline(attribute, false);
		setUseDefault(attribute, false);
		setDefaultValue(attribute, "");
	}




	public static void setCharLimit(TaskAttribute attribute, int limit) {
		attribute.values.put(TEXT_CHAR_LIMIT, limit);
	}




	public static int getCharLimit(TaskAttribute attribute) {
		return attribute.getValue(TEXT_CHAR_LIMIT, Integer.class);
	}




	public static void setMultiline(TaskAttribute attribute, boolean multiline) {
		attribute.values.put(TEXT_MULTILINE, multiline);
	}




	public static boolean getMultiline(TaskAttribute attribute) {
		return attribute.getValue(TEXT_MULTILINE, Boolean.class);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, String defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static String getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, String.class);
	}


}
