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

public class CreatedAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<LocalDateTime> COMPARATOR_ASC = LocalDateTime::compareTo;
	public static final Comparator<LocalDateTime> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, LocalDateTime.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{LocalDateTime.class, LocalDateTime.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{LocalDateTime.class, LocalDateTime.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("CreatedAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.CREATED);
		CreatedAttributeLogic.initAttribute(attribute);
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




	public static boolean isValidTaskValue(TaskAttribute attribute, Object value) {
		return value.getClass() == DATA_TYPES.get(AttributeLogic.ATTRIB_TASK_VALUE_TYPE);
	}




	public static Object generateValidTaskValue(Object oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}


}
