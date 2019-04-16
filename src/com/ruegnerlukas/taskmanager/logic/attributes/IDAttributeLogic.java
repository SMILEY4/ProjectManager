package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.*;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class IDAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Integer.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<Integer> COMPARATOR_ASC = Integer::compareTo;
	public static final Comparator<Integer> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	public static TaskAttribute createAttribute() {
		return createAttribute("IDAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.ID);
		IDAttributeLogic.initAttribute(attribute);
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
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof Integer) || !(values.get(1) instanceof Integer)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof Integer) || !(values.get(1) instanceof Integer)) {
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
			int filterValue = (int) values.get(0);
			return filterValue == (int)taskValue;
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue != (int)taskValue;
		}

		if (operation == FilterOperation.GREATER_THAN) {
			int filterValue = (int) values.get(0);
			return filterValue < (int)taskValue;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue <= (int)taskValue;
		}

		if (operation == FilterOperation.LESS_THAN) {
			int filterValue = (int) values.get(0);
			return filterValue > (int)taskValue;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue >= (int)taskValue;
		}

		if (operation == FilterOperation.IN_RANGE) {
			int filterValueMin = (int) values.get(0);
			int filterValueMax = (int) values.get(1);
			return filterValueMin <= (int)taskValue && (int)taskValue <= filterValueMax;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			int filterValueMin = (int) values.get(0);
			int filterValueMax = (int) values.get(1);
			return !(filterValueMin <= (int)taskValue && (int)taskValue <= filterValueMax);
		}

		return false;
	}

}
