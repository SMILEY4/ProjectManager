package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NumberAttributeLogic {


	public static final String NUMBER_DEC_PLACES = "number_dec_places";
	public static final String NUMBER_MIN_VALUE = "number_min_value";
	public static final String NUMBER_MAX_VALUE = "number_max_value";


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(NUMBER_DEC_PLACES, Integer.class);
		map.put(NUMBER_MIN_VALUE, Double.class);
		map.put(NUMBER_MAX_VALUE, Double.class);
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, Double.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Double.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("NumberAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.NUMBER);
		NumberAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setDecPlaces(attribute, 1);
		setMinValue(attribute, -10);
		setMaxValue(attribute, +10);
		setUseDefault(attribute, false);
		setDefaultValue(attribute, 0);
	}




	public static void setDecPlaces(TaskAttribute attribute, int decPlaces) {
		attribute.values.put(NUMBER_DEC_PLACES, decPlaces);
	}




	public static int getDecPlaces(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_DEC_PLACES, Integer.class);
	}




	public static void setMinValue(TaskAttribute attribute, int minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, (double) minValue);
	}




	public static void setMinValue(TaskAttribute attribute, double minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, minValue);
	}




	public static Number getMinValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MIN_VALUE, Number.class);
	}




	public static void setMaxValue(TaskAttribute attribute, int maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, (double) maxValue);
	}




	public static void setMaxValue(TaskAttribute attribute, double maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, maxValue);
	}




	public static Number getMaxValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MAX_VALUE, Number.class);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, int defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, (double) defaultValue);
	}




	public static void setDefaultValue(TaskAttribute attribute, double defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static Number getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, Number.class);
	}


}
