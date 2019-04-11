package com.ruegnerlukas.taskmanager.data.attributes;

import java.util.Arrays;
import java.util.List;

public class ChoiceAttributeAccess {


	public static final String CHOICE_VALUE_LIST = "choice_value_list";
	public static final String CHOICE_USE_DEFAULT = "choice_use_default";
	public static final String CHOICE_DEFAULT_VALUE = "choice_default_value";




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setValueList(attribute, new String[]{});
		setUseDefault(attribute, false);
		setDefaultValue(attribute, "");
	}




	public static void addValueToList(TaskAttribute attribute, String value) {
		if (!containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = Arrays.copyOf(values, values.length + 1);
			newValues[newValues.length - 1] = value;
			setValueList(attribute, newValues);
		}
	}




	public static void removeValueFromList(TaskAttribute attribute, String value) {
		if (containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = new String[values.length - 1];
			for (int i = 0, j = 0; i < values.length; i++) {
				if (!values[i].equals(value)) {
					newValues[j++] = values[i];
				}
			}
			setValueList(attribute, newValues);
		}
	}




	public static void setValueList(TaskAttribute attribute, List<String> valueList) {
		String[] array = new String[valueList.size()];
		for (int i = 0; i < valueList.size(); i++) {
			array[i] = valueList.get(i);
		}
		setValueList(attribute, array);
	}




	public static void setValueList(TaskAttribute attribute, String... valueList) {
		attribute.values.put(CHOICE_VALUE_LIST, valueList);
	}




	public static String[] getValueList(TaskAttribute attribute) {
		return attribute.getValue(CHOICE_VALUE_LIST, String[].class);
	}




	public static boolean containsValue(TaskAttribute attribute, String value) {
		String[] values = getValueList(attribute);
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				return true;
			}
		}
		return false;
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(CHOICE_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(CHOICE_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, String defaultValue) {
		attribute.values.put(CHOICE_DEFAULT_VALUE, defaultValue);
	}




	public static String getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(CHOICE_DEFAULT_VALUE, String.class);
	}


}
