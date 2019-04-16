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




	public static boolean isValidFilterOperation(Task task, TerminalFilterCriteria criteria) {
		FilterOperation operation = criteria.operation;
		List<Object> values = criteria.values;

		// invalid filter operation
		if (!(operation == FilterOperation.HAS_VALUE
				|| operation == FilterOperation.EQUALS
				|| operation == FilterOperation.EQUALS_IGNORE_CASE
				|| operation == FilterOperation.NOT_EQUALS
				|| operation == FilterOperation.CONTAINS
				|| operation == FilterOperation.CONTAINS_NOT
		)) {
			return false;
		}

		// invalid filter/values
		if (operation == FilterOperation.HAS_VALUE) {
			if (values.size() != 1 || !(values.get(0) instanceof Boolean)) {
				return false;
			}
		}
		if (operation == FilterOperation.EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof String)) {
				return false;
			}
		}
		if (operation == FilterOperation.EQUALS_IGNORE_CASE) {
			if (values.size() != 1 || !(values.get(0) instanceof String)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof String)) {
				return false;
			}
		}
		if (operation == FilterOperation.CONTAINS) {
			if (values.size() != 1 || !(values.get(0) instanceof String)) {
				return false;
			}
		}
		if (operation == FilterOperation.CONTAINS_NOT) {
			if (values.size() != 1 || !(values.get(0) instanceof String)) {
				return false;
			}
		}

		return true;
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {
		TaskAttribute attribute = criteria.attribute.get();
		FilterOperation operation = criteria.operation;
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
			String filterValue = (String) values.get(0);
			return filterValue.equals((String) taskValue);
		}

		if (operation == FilterOperation.EQUALS_IGNORE_CASE) {
			String filterValue = (String) values.get(0);
			return filterValue.equalsIgnoreCase((String) taskValue);
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			String filterValue = (String) values.get(0);
			return !filterValue.equals((String) taskValue);
		}

		if (operation == FilterOperation.CONTAINS) {
			String filterValue = (String) values.get(0);
			return ((String) taskValue).contains(filterValue);
		}

		if (operation == FilterOperation.CONTAINS_NOT) {
			String filterValue = (String) values.get(0);
			return !((String) taskValue).contains(filterValue);
		}


		return false;
	}

}
