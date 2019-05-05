package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class BooleanAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Boolean> COMPARATOR_ASC = Boolean::compare;
	public static final Comparator<Boolean> COMPARATOR_DESC = (x, y) -> Boolean.compare(x, y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Boolean.class);
		mapTypes.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, Boolean.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Boolean.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("BooleanAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.BOOLEAN);
		BooleanAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, false);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, boolean defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static boolean getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, Boolean.class);
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {
		TaskAttribute attribute = criteria.attribute.get();
		FilterOperation operation = criteria.operation.get();
		List<Object> values = criteria.values;
		Object taskValue = TaskLogic.getValue(task, attribute);

		if (operation == FilterOperation.HAS_VALUE) {
			boolean filterValue = (Boolean) values.get(0);
			if (filterValue) {
				return taskValue != null && !(taskValue instanceof NoValue);
			} else {
				return taskValue == null || (taskValue instanceof NoValue);
			}
		}

		if (operation == FilterOperation.EQUALS) {
			boolean filterValue = (boolean) values.get(0);
			return filterValue == (boolean) taskValue;
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			boolean filterValue = (boolean) values.get(0);
			return filterValue != (boolean) taskValue;
		}

		return false;
	}




	public static boolean isValidTaskValue(TaskAttribute attribute, Object value) {
		return value.getClass() == DATA_TYPES.get(AttributeLogic.ATTRIB_TASK_VALUE_TYPE) || value.getClass() == NoValue.class;
	}




	public static Object generateValidTaskValue(Object oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return preferNoValue ? new NoValue() : false;
	}


}
