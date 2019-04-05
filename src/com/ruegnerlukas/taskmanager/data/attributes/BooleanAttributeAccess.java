package com.ruegnerlukas.taskmanager.data.attributes;

public class BooleanAttributeAccess {


	public static final String BOOLEAN_USE_DEFAULT = "bool_use_default";
	public static final String BOOLEAN_DEFAULT_VALUE = "bool_default_value";




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
