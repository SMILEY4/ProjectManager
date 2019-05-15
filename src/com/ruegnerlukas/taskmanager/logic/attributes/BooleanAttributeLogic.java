package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.BoolValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class BooleanAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Boolean> COMPARATOR_ASC = Boolean::compare;
	public static final Comparator<Boolean> COMPARATOR_DESC = (x, y) -> Boolean.compare(x, y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(TaskAttribute.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, BoolValue.class);
		mapTypes.put(TaskAttribute.ATTRIB_TASK_VALUE_TYPE, BoolValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Boolean.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("BooleanAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.BOOLEAN);
		BooleanAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new BoolValue(false));
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(TaskAttribute.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_USE_DEFAULT);
	}




	public static void setDefaultValue(TaskAttribute attribute, BoolValue defaultValue) {
		attribute.values.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static BoolValue getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_DEFAULT_VALUE);
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return filterValues.get(0) == ((BoolValue) valueTask).getValue();
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return filterValues.get(0) != ((BoolValue) valueTask).getValue();
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
		return value.getAttType() == null || value.getAttType() == AttributeType.BOOLEAN;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return preferNoValue ? new NoValue() : new BoolValue(false);
	}


}
