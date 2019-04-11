package com.ruegnerlukas.taskmanager.data.attributes;

import java.util.Arrays;

public class TaskFlagAttributeAccess {


	public static final String FLAG_FLAG_LIST = "flag_flag_list";
	public static final String FLAG_DEFAULT_VALUE = "flag_default_value";




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		TaskFlag defaultFlag = new TaskFlag("Default", TaskFlag.FlagColor.GRAY);
		setFlagList(attribute, new TaskFlag[]{defaultFlag});
		setDefaultValue(attribute, defaultFlag);
	}




	public static void addFlagToList(TaskAttribute attribute, TaskFlag flag) {
		if(!containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = Arrays.copyOf(list, list.length + 1);
			newList[newList.length - 1] = flag;
			setFlagList(attribute, newList);
		}
	}




	public static void removeFlagFromList(TaskAttribute attribute, TaskFlag flag) {
		if (containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = new TaskFlag[list.length - 1];
			for (int i = 0, j = 0; i < list.length; i++) {
				if (list[i] != flag) {
					newList[j++] = list[i];
				}
			}
			setFlagList(attribute, newList);
		}
	}




	public static void setFlagList(TaskAttribute attribute, TaskFlag[] list) {
		attribute.values.put(FLAG_FLAG_LIST, list);
	}




	public static TaskFlag[] getFlagList(TaskAttribute attribute) {
		return attribute.getValue(FLAG_FLAG_LIST, TaskFlag[].class);
	}




	public static boolean containsFlag(TaskAttribute attribute, TaskFlag flag) {
		TaskFlag[] list = getFlagList(attribute);
		for (int i = 0; i < list.length; i++) {
			if (list[i] == flag) {
				return true;
			}
		}
		return false;
	}




	public static void setDefaultValue(TaskAttribute attribute, TaskFlag defaultValue) {
		attribute.values.put(FLAG_DEFAULT_VALUE, defaultValue);
	}




	public static TaskFlag getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(FLAG_DEFAULT_VALUE, TaskFlag.class);
	}


}
