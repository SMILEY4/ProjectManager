package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class TaskFlagAttributeLogic {


	public static final String FLAG_FLAG_LIST = "flag_flag_list";

	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<TaskFlag> COMPARATOR_ASC = Comparator.comparing(x -> x.name.get());
	public static final Comparator<TaskFlag> COMPARATOR_DESC = (x, y) -> x.name.get().compareTo(y.name.get()) * -1;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(FLAG_FLAG_LIST, TaskFlag[].class);
		map.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		map.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, FlagValue.class);
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, FlagValue.class);
		DATA_TYPES = Collections.unmodifiableMap(map);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{TaskFlag.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{TaskFlag.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("FlagAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.FLAG);
		TaskFlagAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		TaskFlag defaultFlag = new TaskFlag("Default", TaskFlag.FlagColor.GRAY);
		setFlagList(attribute, new TaskFlag[]{defaultFlag});
		setUseDefault(attribute, true);
		setDefaultValue(attribute, new FlagValue(defaultFlag));
	}




	public static void addFlagToList(TaskAttribute attribute, TaskFlag flag) {
		if (!containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = Arrays.copyOf(list, list.length + 1);
			newList[newList.length - 1] = flag;
			setFlagList(attribute, newList);
		}
	}




	public static void removeFlagFromList(TaskAttribute attribute, TaskFlag flag) {
		if (containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = new TaskFlag[list.length - 1];
			for (int i = 0, j = 0; i < list.length; i++) {
				if (list[i] != flag) {
					newList[j++] = list[i];
				}
			}
			setFlagList(attribute, newList);
		}
	}




	public static void setFlagList(TaskAttribute attribute, TaskFlag[] list) {
		attribute.values.put(FLAG_FLAG_LIST, list);
	}




	public static TaskFlag[] getFlagList(TaskAttribute attribute) {
		return attribute.getValue(FLAG_FLAG_LIST);
	}




	public static boolean containsFlag(TaskAttribute attribute, TaskFlag flag) {
		TaskFlag[] list = getFlagList(attribute);
		for (int i = 0; i < list.length; i++) {
			if (list[i] == flag) {
				return true;
			}
		}
		return false;
	}




	private static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT);
	}




	private static void setDefaultValue(TaskAttribute attribute, FlagValue defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static FlagValue getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE);
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {
		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof TaskFlag) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((FlagValue) valueTask).getValue() == filterValues.get(0);
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof TaskFlag) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((FlagValue) valueTask).getValue() != filterValues.get(0);
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
		if (value.getAttType() == AttributeType.FLAG) {
			FlagValue valueFlag = (FlagValue) value;
			return ArrayUtils.contains(getFlagList(attribute), valueFlag.getValue());
		} else {
			return value.getAttType() == null;
		}
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		TaskFlag[] flags = getFlagList(attribute);
		if (flags.length == 0) {
			return getDefaultValue(attribute);
		} else {
			return new FlagValue(flags[0]);
		}
	}

}
