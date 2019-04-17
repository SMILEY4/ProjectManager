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

public class IDAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Integer> COMPARATOR_ASC = Integer::compareTo;
	public static final Comparator<Integer> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Integer.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




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
			int filterValue = (int) values.get(0);
			return filterValue == (int) taskValue;
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue != (int) taskValue;
		}

		if (operation == FilterOperation.GREATER_THAN) {
			int filterValue = (int) values.get(0);
			return filterValue < (int) taskValue;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue <= (int) taskValue;
		}

		if (operation == FilterOperation.LESS_THAN) {
			int filterValue = (int) values.get(0);
			return filterValue > (int) taskValue;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			int filterValue = (int) values.get(0);
			return filterValue >= (int) taskValue;
		}

		if (operation == FilterOperation.IN_RANGE) {
			int filterValueMin = (int) values.get(0);
			int filterValueMax = (int) values.get(1);
			return filterValueMin <= (int) taskValue && (int) taskValue <= filterValueMax;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			int filterValueMin = (int) values.get(0);
			int filterValueMax = (int) values.get(1);
			return !(filterValueMin <= (int) taskValue && (int) taskValue <= filterValueMax);
		}

		return false;
	}

}
