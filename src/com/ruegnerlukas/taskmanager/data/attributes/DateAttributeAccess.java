package com.ruegnerlukas.taskmanager.data.attributes;

import java.time.LocalDate;

public class DateAttributeAccess {


	public static final String DATE_USE_DEFAULT = "date_use_default";
	public static final String DATE_DEFAULT_VALUE = "date_default_value";




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
