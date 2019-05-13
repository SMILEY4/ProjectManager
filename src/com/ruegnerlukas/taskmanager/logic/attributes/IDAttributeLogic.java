package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class IDAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Integer> COMPARATOR_ASC = Integer::compareTo;
	public static final Comparator<Integer> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, IDValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("IDAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.ID);
		IDAttributeLogic.initAttribute(attribute);
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Integer && filterValues.get(1) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						int min = (Integer) filterValues.get(0);
						int max = (Integer) filterValues.get(1);
						int id = ((IDValue) valueTask).getValue();
						return min <= id && max >= id;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Integer && filterValues.get(1) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						int min = (Integer) filterValues.get(0);
						int max = (Integer) filterValues.get(1);
						int id = ((IDValue) valueTask).getValue();
						return !(min <= id && max >= id);
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
		return value.getAttType() == AttributeType.ID;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}


}
