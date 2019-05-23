package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class DependencyAttributeLogic {


	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Task[]> COMPARATOR_ASC = Comparator.comparingInt(x -> x.length);
	public static final Comparator<Task[]> COMPARATOR_DESC = (x, y) -> Integer.compare(x.length, y.length) * -1;




	static {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.DEPENDENT_ON, new Class<?>[]{Task.class});
		mapData.put(FilterOperation.PREREQUISITE_OF, new Class<?>[]{Task.class});
		mapData.put(FilterOperation.INDEPENDENT, new Class<?>[]{Boolean.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




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




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case DEPENDENT_ON: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Task) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						Task tasksFilter = (Task) filterValues.get(0);
						List<Task> list = Arrays.asList((Task[]) ((DependencyValue) valueTask).getValue());
						return list.contains(tasksFilter);
					}
				} else {
					return false;
				}
			}

			case PREREQUISITE_OF: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Task) {
					Task tasksFilter = (Task) filterValues.get(0);
					TaskValue<?> valueFilter = TaskLogic.getValueOrDefault(tasksFilter, criteria.attribute.get());
					if (valueFilter.getAttType() != null) {
						List<Task> list = Arrays.asList(((DependencyValue) valueFilter).getValue());
						if (list.contains(task)) {
							return true;
						}
					}
					return false;
				}
			}

			case INDEPENDENT: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					boolean isIndependent = (boolean) filterValues.get(0);
					if (valueTask.getAttType() == null) {
						return isIndependent;
					} else {
						if (isIndependent) {
							return ((DependencyValue) valueTask).getValue().length == 0;
						} else {
							return ((DependencyValue) valueTask).getValue().length != 0;
						}
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
		return value.getAttType() == AttributeType.DEPENDENCY || value.getAttType() == null;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return new NoValue();
	}

}
