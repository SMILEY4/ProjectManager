package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

public class NumberAttributeLogic {


	public static final String NUMBER_DEC_PLACES = "number_dec_places";
	public static final String NUMBER_MIN_VALUE = "number_min_value";
	public static final String NUMBER_MAX_VALUE = "number_max_value";
	public static final String NUMBER_USE_DEFAULT = "number_use_default";
	public static final String NUMBER_DEFAULT_VALUE = "number_default_value";




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
		attribute.values.put(NUMBER_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, int defaultValue) {
		attribute.values.put(NUMBER_DEFAULT_VALUE, (double) defaultValue);
	}




	public static void setDefaultValue(TaskAttribute attribute, double defaultValue) {
		attribute.values.put(NUMBER_DEFAULT_VALUE, defaultValue);
	}




	public static Number getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_DEFAULT_VALUE, Number.class);
	}


}
