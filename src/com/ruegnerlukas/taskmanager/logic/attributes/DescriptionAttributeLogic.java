package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DescriptionValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class DescriptionAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<String> COMPARATOR_ASC = String::compareTo;
	public static final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(TaskAttribute.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, DescriptionValue.class);
		mapTypes.put(TaskAttribute.ATTRIB_TASK_VALUE_TYPE, DescriptionValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.EQUALS_IGNORE_CASE, new Class<?>[]{String.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.CONTAINS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.CONTAINS_NOT, new Class<?>[]{String.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("DescriptionAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DESCRIPTION);
		DescriptionAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, true);
		setDefaultValue(attribute, new DescriptionValue(""));
	}




	private static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(TaskAttribute.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_USE_DEFAULT);
	}




	private static void setDefaultValue(TaskAttribute attribute, DescriptionValue defaultValue) {
		attribute.values.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static DescriptionValue getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_DEFAULT_VALUE);
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().equals((String) filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case EQUALS_IGNORE_CASE: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().equalsIgnoreCase((String) filterValues.get(0));
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
						return !((DescriptionValue) valueTask).getValue().equals((String) filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case CONTAINS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().contains((String) filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case CONTAINS_NOT: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return !((DescriptionValue) valueTask).getValue().contains((String) filterValues.get(0));
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
		return value.getAttType() == AttributeType.DESCRIPTION;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}

}