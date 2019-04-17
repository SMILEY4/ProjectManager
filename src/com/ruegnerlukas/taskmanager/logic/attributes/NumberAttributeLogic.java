package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class NumberAttributeLogic {


	public static final String NUMBER_DEC_PLACES = "number_dec_places";
	public static final String NUMBER_MIN_VALUE = "number_min_value";
	public static final String NUMBER_MAX_VALUE = "number_max_value";

	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Double> COMPARATOR_ASC = Double::compareTo;
	public static final Comparator<Double> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(NUMBER_DEC_PLACES, Integer.class);
		mapTypes.put(NUMBER_MIN_VALUE, Double.class);
		mapTypes.put(NUMBER_MAX_VALUE, Double.class);
		mapTypes.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, Double.class);
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Double.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{Double.class, Double.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{Double.class, Double.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("NumberAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.NUMBER);
		NumberAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setDecPlaces(attribute, 1);
		setMinValue(attribute, -10);
		setMaxValue(attribute, +10);
		setUseDefault(attribute, false);
		setDefaultValue(attribute, 0);
	}




	public static void setDecPlaces(TaskAttribute attribute, int decPlaces) {
		attribute.values.put(NUMBER_DEC_PLACES, decPlaces);
	}




	public static int getDecPlaces(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_DEC_PLACES, Integer.class);
	}




	public static void setMinValue(TaskAttribute attribute, int minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, (double) minValue);
	}




	public static void setMinValue(TaskAttribute attribute, double minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, minValue);
	}




	public static Number getMinValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MIN_VALUE, Number.class);
	}




	public static void setMaxValue(TaskAttribute attribute, int maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, (double) maxValue);
	}




	public static void setMaxValue(TaskAttribute attribute, double maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, maxValue);
	}




	public static Number getMaxValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MAX_VALUE, Number.class);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	public static void setDefaultValue(TaskAttribute attribute, int defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, (double) defaultValue);
	}




	public static void setDefaultValue(TaskAttribute attribute, double defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static Number getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, Number.class);
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
			double filterValue = (double) values.get(0);
			return MathUtils.isNearlyEqual(filterValue, (double) taskValue, getDecPlaces(attribute));
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			double filterValue = (double) values.get(0);
			return !MathUtils.isNearlyEqual(filterValue, (double) taskValue, getDecPlaces(attribute));
		}

		if (operation == FilterOperation.GREATER_THAN) {
			double filterValue = (double) values.get(0);
			return filterValue < (double) taskValue;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			double filterValue = (double) values.get(0);
			return filterValue <= (double) taskValue;
		}

		if (operation == FilterOperation.LESS_THAN) {
			double filterValue = (double) values.get(0);
			return filterValue > (double) taskValue;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			double filterValue = (double) values.get(0);
			return filterValue >= (double) taskValue;
		}

		if (operation == FilterOperation.IN_RANGE) {
			double filterValueMin = (double) values.get(0);
			double filterValueMax = (double) values.get(1);
			return filterValueMin <= (double) taskValue && (double) taskValue <= filterValueMax;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			double filterValueMin = (double) values.get(0);
			double filterValueMax = (double) values.get(1);
			return !(filterValueMin <= (double) taskValue && (double) taskValue <= filterValueMax);
		}

		return false;
	}

}
