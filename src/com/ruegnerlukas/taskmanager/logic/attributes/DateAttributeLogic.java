package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.LocalDate;
import java.util.*;

public class DateAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, LocalDate.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, LocalDate.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<LocalDate> COMPARATOR_ASC = LocalDate::compareTo;
	public static final Comparator<LocalDate> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	public static TaskAttribute createAttribute() {
		return createAttribute("DateAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DATE);
		DateAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, LocalDate.now());
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, LocalDate defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static LocalDate getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, LocalDate.class);
	}




	public static boolean isValidFilterOperation(Task task, TerminalFilterCriteria criteria) {
		FilterOperation operation = criteria.operation;
		List<Object> values = criteria.values;

		// invalid filter operation
		if (!(operation == FilterOperation.HAS_VALUE
				|| operation == FilterOperation.EQUALS
				|| operation == FilterOperation.NOT_EQUALS
				|| operation == FilterOperation.GREATER_THAN
				|| operation == FilterOperation.GREATER_EQUALS
				|| operation == FilterOperation.LESS_THAN
				|| operation == FilterOperation.LESS_EQUALS
				|| operation == FilterOperation.IN_RANGE
				|| operation == FilterOperation.NOT_IN_RANGE
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
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof LocalDate) || !(values.get(1) instanceof LocalDate)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof LocalDate) || !(values.get(1) instanceof LocalDate)) {
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
			LocalDate filterValue = (LocalDate) values.get(0);
			return filterValue.equals((LocalDate) taskValue);
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			LocalDate filterValue = (LocalDate) values.get(0);
			return !filterValue.equals((LocalDate) taskValue);
		}

		if (operation == FilterOperation.GREATER_THAN) {
			LocalDate filterValue = (LocalDate) values.get(0);
			final int cmp = ((LocalDate) taskValue).compareTo(filterValue);
			return cmp > 0;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			LocalDate filterValue = (LocalDate) values.get(0);
			final int cmp = ((LocalDate) taskValue).compareTo(filterValue);
			return cmp >= 0;
		}

		if (operation == FilterOperation.LESS_THAN) {
			LocalDate filterValue = (LocalDate) values.get(0);
			final int cmp = ((LocalDate) taskValue).compareTo(filterValue);
			return cmp < 0;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			LocalDate filterValue = (LocalDate) values.get(0);
			final int cmp = ((LocalDate) taskValue).compareTo(filterValue);
			return cmp <= 0;
		}

		if (operation == FilterOperation.IN_RANGE) {
			LocalDate filterValueMin = (LocalDate) values.get(0);
			LocalDate filterValueMax = (LocalDate) values.get(1);
			final int cmpMin = ((LocalDate) taskValue).compareTo(filterValueMin);
			final int cmpMax = ((LocalDate) taskValue).compareTo(filterValueMax);
			return cmpMin >= 0 && cmpMax <= 0;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			LocalDate filterValueMin = (LocalDate) values.get(0);
			LocalDate filterValueMax = (LocalDate) values.get(1);
			final int cmpMin = ((LocalDate) taskValue).compareTo(filterValueMin);
			final int cmpMax = ((LocalDate) taskValue).compareTo(filterValueMax);
			return !(cmpMin >= 0 && cmpMax <= 0);
		}


		return false;
	}

}
