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

public class DependencyAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;




	static {
		Map<String, Class<?>> map = new HashMap<>();
		map.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, Task[].class);
		DATA_TYPES = Collections.unmodifiableMap(map);
	}




	public static final Comparator<Task[]> COMPARATOR_ASC = Comparator.comparingInt(x -> x.length);
	public static final Comparator<Task[]> COMPARATOR_DESC = (x, y) -> Integer.compare(x.length, y.length) * -1;




	public static TaskAttribute createAttribute() {
		return createAttribute("DependencyAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DEPENDENCY);
		DependencyAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
	}




	private static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
	}




	private static void setDefaultValue(TaskAttribute attribute, LocalDateTime defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static LocalDateTime getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE, LocalDateTime.class);
	}




	public static boolean isValidFilterOperation(Task task, TerminalFilterCriteria criteria) {
		FilterOperation operation = criteria.operation;
		List<Object> values = criteria.values;

		// invalid filter operation
		if (!(operation == FilterOperation.HAS_VALUE
				|| operation == FilterOperation.DEPENDENT_ON
				|| operation == FilterOperation.PREREQUISITE_OF
				|| operation == FilterOperation.INDEPENDENT
		)) {
			return false;
		}

		// invalid filter/values
		if (operation == FilterOperation.HAS_VALUE) {
			if (values.size() != 1 || !(values.get(0) instanceof Boolean)) {
				return false;
			}
		}
		if (operation == FilterOperation.DEPENDENT_ON) {
			if (values.size() != 1 || !(values.get(0) instanceof Task)) {
				return false;
			}
		}
		if (operation == FilterOperation.PREREQUISITE_OF) {
			if (values.size() != 1 || !(values.get(0) instanceof Task)) {
				return false;
			}
		}
		if (operation == FilterOperation.INDEPENDENT) {
			if (values.size() != 1 || !(values.get(0) instanceof Boolean)) {
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

		if (operation == FilterOperation.DEPENDENT_ON) {
			Task[] tasksFilter = (Task[]) values.get(0);
			List<Task> list = Arrays.asList((Task[]) taskValue);
			for (Task t : tasksFilter) {
				if (list.contains(t)) {
					return true;
				}
			}
			return false;
		}

		if (operation == FilterOperation.PREREQUISITE_OF) {
			Task[] tasksFilter = (Task[]) values.get(0);
			for (Task t : tasksFilter) {
				List<Task> list = Arrays.asList((Task[]) TaskLogic.getValue(t, attribute));
				if (list.contains(task)) {
					return true;
				}
			}
			return false;
		}

		if (operation == FilterOperation.INDEPENDENT) {
			return ((Task[]) taskValue).length == 0;
		}

		return false;
	}


}
