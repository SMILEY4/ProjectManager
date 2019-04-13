package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeLogic {


	public static final String ATTRIB_TASK_VALUE_TYPE = "attrib_task_value_type";
	public static final String ATTRIB_USE_DEFAULT = "attrib_use_default";
	public static final String ATTRIB_DEFAULT_VALUE = "attrib_default_value";


	public static final Map<AttributeType, Class<?>> LOGIC_CLASSED = new HashMap<>();




	static {
		LOGIC_CLASSED.put(AttributeType.BOOLEAN, BooleanAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.CHOICE, ChoiceAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.CREATED, CreatedAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.DATE, DateAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.DEPENDENCY, DependencyAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.DESCRIPTION, DescriptionAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.ID, IDAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.LAST_UPDATED, LastUpdatedAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.NUMBER, NumberAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.FLAG, TaskFlagAttributeLogic.class);
		LOGIC_CLASSED.put(AttributeType.TEXT, TextAttributeLogic.class);

	}




	public static TaskAttribute createTaskAttribute(AttributeType type) {
		return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()));
	}




	public static TaskAttribute createTaskAttribute(AttributeType type, String name) {
		try {
			Method method = LOGIC_CLASSED.get(type).getMethod("createAttribute", String.class);
			TaskAttribute attribute = (TaskAttribute) method.invoke(null, name);
			return attribute;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}




	public static void initTaskAttribute(TaskAttribute attribute) {
		try {
			Method method = LOGIC_CLASSED.get(attribute.type.get()).getMethod("initAttribute", TaskAttribute.class);
			method.invoke(null, attribute);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
		try {
			Field field = LOGIC_CLASSED.get(attribute.type.get()).getField("DATA_TYPES");
			Map<String, Class<?>> map = (Map<String, Class<?>>) field.get(null);
			if (newValue.getClass() != map.get(key)) {
				return false;
			}
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		attribute.values.put(key, newValue);
		return true;
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
