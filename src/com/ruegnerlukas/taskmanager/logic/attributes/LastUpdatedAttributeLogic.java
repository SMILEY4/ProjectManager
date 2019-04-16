package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.LocalDateTime;
import java.util.*;

public class LastUpdatedAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, LocalDateTime.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<LocalDateTime> COMPARATOR_ASC = LocalDateTime::compareTo;
	public static final Comparator<LocalDateTime> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	public static TaskAttribute createAttribute() {
		return createAttribute("LastUpdatedAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.LAST_UPDATED);
		LastUpdatedAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
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
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof LocalDateTime) || !(values.get(1) instanceof LocalDateTime)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof LocalDateTime) || !(values.get(1) instanceof LocalDateTime)) {
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
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			return filterValue.equals((LocalDateTime) taskValue);
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			return !filterValue.equals((LocalDateTime) taskValue);
		}

		if (operation == FilterOperation.GREATER_THAN) {
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			final int cmp = ((LocalDateTime) taskValue).compareTo(filterValue);
			return cmp > 0;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			final int cmp = ((LocalDateTime) taskValue).compareTo(filterValue);
			return cmp >= 0;
		}

		if (operation == FilterOperation.LESS_THAN) {
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			final int cmp = ((LocalDateTime) taskValue).compareTo(filterValue);
			return cmp < 0;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			LocalDateTime filterValue = (LocalDateTime) values.get(0);
			final int cmp = ((LocalDateTime) taskValue).compareTo(filterValue);
			return cmp <= 0;
		}

		if (operation == FilterOperation.IN_RANGE) {
			LocalDateTime filterValueMin = (LocalDateTime) values.get(0);
			LocalDateTime filterValueMax = (LocalDateTime) values.get(1);
			final int cmpMin = ((LocalDateTime) taskValue).compareTo(filterValueMin);
			final int cmpMax = ((LocalDateTime) taskValue).compareTo(filterValueMax);
			return cmpMin >= 0 && cmpMax <= 0;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			LocalDateTime filterValueMin = (LocalDateTime) values.get(0);
			LocalDateTime filterValueMax = (LocalDateTime) values.get(1);
			final int cmpMin = ((LocalDateTime) taskValue).compareTo(filterValueMin);
			final int cmpMax = ((LocalDateTime) taskValue).compareTo(filterValueMax);
			return !(cmpMin >= 0 && cmpMax <= 0);
		}

		return false;
	}

}
