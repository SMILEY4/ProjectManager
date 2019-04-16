package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeLogic {


	public static final String ATTRIB_TASK_VALUE_TYPE = "attrib_task_value_type";
	public static final String ATTRIB_USE_DEFAULT = "attrib_use_default";
	public static final String ATTRIB_DEFAULT_VALUE = "attrib_default_value";




	public static TaskAttribute createTaskAttribute(AttributeType type) {
		return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()));
	}




	public static TaskAttribute createTaskAttribute(AttributeType type, String name) {
		return AttributeLogicManager.createTaskAttribute(type, name);
	}




	public static void initTaskAttribute(TaskAttribute attribute) {
		AttributeLogicManager.initeTaskAttribute(attribute);
	}




	public static String renameTaskAttribute(Project project, TaskAttribute attribute, String newName) {
		for (TaskAttribute attrib : project.data.attributes) {
			if (attrib == attribute) {
				continue;
			}
			if (attrib.name.get().equalsIgnoreCase(newName)) {
				return attribute.name.get();
			}
		}
		attribute.name.set(newName);
		return attribute.name.get();
	}




	public static void setTaskAttributeType(TaskAttribute attribute, AttributeType newType) {
		attribute.type.set(newType);
		initTaskAttribute(attribute);
	}




	public static boolean setTaskAttributeValue(TaskAttribute attribute, String key, Object newValue) {

		// validate value
		Map<String, Class<?>> map = AttributeLogicManager.getDataTypeMap(attribute.type.get());
		if (newValue.getClass() != map.get(key)) {
			return false;
		}

		attribute.values.put(key, newValue);
		return true;
	}




	public static TaskAttribute findAttribute(Project project, String name) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.name.get().equals(name)) {
				return attribute;
			}
		}
		return null;
	}




	public static TaskAttribute findAttribute(Project project, AttributeType type) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				return attribute;
			}
		}
		return null;
	}




	public static List<TaskAttribute> findAttributes(Project project, AttributeType type) {
		List<TaskAttribute> list = new ArrayList<>();
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				list.add(attribute);
			}
		}
		return list;
	}




	public static boolean getUsesDefault(TaskAttribute attribute) {
		Boolean value = attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		if (value != null) {
			return value;
		} else {
			return false;
		}
	}




	public static boolean hasDefaultValueDefined(TaskAttribute attribute) {
		return attribute.values.containsKey(AttributeLogic.ATTRIB_DEFAULT_VALUE);
	}




	public static Object getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, Object.class);
	}


}
