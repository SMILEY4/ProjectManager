package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.ChoiceValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class ChoiceAttributeLogic {


	public static final String CHOICE_VALUE_LIST = "choice_value_list";

	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<String> COMPARATOR_ASC = String::compareTo;
	public static final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(CHOICE_VALUE_LIST, String[].class);
		mapTypes.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, ChoiceValue.class);
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, ChoiceValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{String.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("ChoiceAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.CHOICE);
		ChoiceAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setValueList(attribute, new String[]{});
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new ChoiceValue(""));
	}




	public static void addValueToList(TaskAttribute attribute, String value) {
		if (!containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = Arrays.copyOf(values, values.length + 1);
			newValues[newValues.length - 1] = value;
			setValueList(attribute, newValues);
		}
	}




	public static void removeValueFromList(TaskAttribute attribute, String value) {
		if (containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = new String[values.length - 1];
			for (int i = 0, j = 0; i < values.length; i++) {
				if (!values[i].equals(value)) {
					newValues[j++] = values[i];
				}
			}
			setValueList(attribute, newValues);
		}
	}




	public static void setValueList(TaskAttribute attribute, List<String> valueList) {
		String[] array = new String[valueList.size()];
		for (int i = 0; i < valueList.size(); i++) {
			array[i] = valueList.get(i);
		}
		setValueList(attribute, array);
	}




	public static void setValueList(TaskAttribute attribute, String... valueList) {
		attribute.values.put(CHOICE_VALUE_LIST, valueList);
	}




	public static String[] getValueList(TaskAttribute attribute) {
		return attribute.getValue(CHOICE_VALUE_LIST);
	}




	public static boolean containsValue(TaskAttribute attribute, String value) {
		String[] values = getValueList(attribute);
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				return true;
			}
		}
		return false;
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT);
	}




	public static void setDefaultValue(TaskAttribute attribute, ChoiceValue defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static ChoiceValue getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE);
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case HAS_VALUE: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					boolean valueFilter = (Boolean) filterValues.get(0);
					return valueFilter == (valueTask.getAttType() == null);
				} else {
					return false;
				}
			}

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((String) filterValues.get(0)).equalsIgnoreCase(((ChoiceValue) valueTask).getValue());
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return !((String) filterValues.get(0)).equalsIgnoreCase(((ChoiceValue) valueTask).getValue());
					}
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}

	}




	public static boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value) {
		if (value.getAttType() == null) {
			return true;
		} else if (value.getAttType() == AttributeType.CHOICE) {
			return ArrayUtils.contains(getValueList(attribute), ((ChoiceValue) value).getValue());
		} else {
			return false;
		}
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		if (preferNoValue || getValueList(attribute).length == 0) {
			return new NoValue();
		} else {
			return new ChoiceValue(getValueList(attribute)[0]);
		}
	}

}