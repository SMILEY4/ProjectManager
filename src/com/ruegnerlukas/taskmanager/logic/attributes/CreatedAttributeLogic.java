package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.CreatedValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
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
		mapTypes.put(TaskAttribute.ATTRIB_TASK_VALUE_TYPE, CreatedValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
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

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDateTime && filterValues.get(1) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDateTime min = (LocalDateTime) filterValues.get(0);
						LocalDateTime max = (LocalDateTime) filterValues.get(1);
						LocalDateTime time = ((CreatedValue) valueTask).getValue();
						return min.compareTo(time) <= 0 && max.compareTo(time) >= 0;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDateTime && filterValues.get(1) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDateTime min = (LocalDateTime) filterValues.get(0);
						LocalDateTime max = (LocalDateTime) filterValues.get(1);
						LocalDateTime time = ((CreatedValue) valueTask).getValue();
						return !(min.compareTo(time) <= 0 && max.compareTo(time) >= 0);
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
		return value.getAttType() == AttributeType.CREATED;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}


}
