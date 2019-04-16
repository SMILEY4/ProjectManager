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




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(NUMBER_DEC_PLACES, Integer.class);
		map.put(NUMBER_MIN_VALUE, Double.class);
		map.put(NUMBER_MAX_VALUE, Double.class);
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, Double.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Double.class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<Double> COMPARATOR_ASC = Double::compareTo;
	public static final Comparator<Double> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




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
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.GREATER_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_THAN) {
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.LESS_EQUALS) {
			if (values.size() != 1 || !(values.get(0) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof Double) || !(values.get(1) instanceof Double)) {
				return false;
			}
		}
		if (operation == FilterOperation.NOT_IN_RANGE) {
			if (values.size() != 2 || !(values.get(0) instanceof Double) || !(values.get(1) instanceof Double)) {
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
			double filterValue = (double) values.get(0);
			return MathUtils.isNearlyEqual(filterValue, (double)taskValue, getDecPlaces(attribute));
		}

		if (operation == FilterOperation.NOT_EQUALS) {
			double filterValue = (double) values.get(0);
			return !MathUtils.isNearlyEqual(filterValue, (double)taskValue, getDecPlaces(attribute));
		}

		if (operation == FilterOperation.GREATER_THAN) {
			double filterValue = (double) values.get(0);
			return filterValue < (double)taskValue;
		}

		if (operation == FilterOperation.GREATER_EQUALS) {
			double filterValue = (double) values.get(0);
			return filterValue <= (double)taskValue;
		}

		if (operation == FilterOperation.LESS_THAN) {
			double filterValue = (double) values.get(0);
			return filterValue > (double)taskValue;
		}

		if (operation == FilterOperation.LESS_EQUALS) {
			double filterValue = (double) values.get(0);
			return filterValue >= (double)taskValue;
		}

		if (operation == FilterOperation.IN_RANGE) {
			double filterValueMin = (double) values.get(0);
			double filterValueMax = (double) values.get(1);
			return filterValueMin <= (double)taskValue && (double)taskValue <= filterValueMax;
		}

		if (operation == FilterOperation.NOT_IN_RANGE) {
			double filterValueMin = (double) values.get(0);
			double filterValueMax = (double) values.get(1);
			return !(filterValueMin <= (double)taskValue && (double)taskValue <= filterValueMax);
		}

		return false;
	}

}
