package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DateValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.LocalDate;
import java.util.*;

public class DateAttributeLogic {


	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<LocalDate> COMPARATOR_ASC = LocalDate::compareTo;
	public static final Comparator<LocalDate> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(AttributeLogic.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, DateValue.class);
		mapTypes.put(AttributeLogic.ATTRIB_TASK_VALUE_TYPE, DateValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{LocalDate.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{LocalDate.class, LocalDate.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{LocalDate.class, LocalDate.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	public static TaskAttribute createAttribute() {
		return createAttribute("DateAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public static TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.DATE);
		DateAttributeLogic.initAttribute(attribute);
		return attribute;
	}




	public static void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new DateValue(LocalDate.now()));
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT);
	}




	public static void setDefaultValue(TaskAttribute attribute, DateValue defaultValue) {
		attribute.values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static DateValue getDefaultValue(TaskAttribute attribute) {
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DateValue) valueTask).getValue().compareTo((LocalDate) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDate && filterValues.get(1) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDate min = (LocalDate) filterValues.get(0);
						LocalDate max = (LocalDate) filterValues.get(1);
						LocalDate time = ((DateValue) valueTask).getValue();
						return min.compareTo(time) <= 0 && max.compareTo(time) >= 0;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDate && filterValues.get(1) instanceof LocalDate) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDate min = (LocalDate) filterValues.get(0);
						LocalDate max = (LocalDate) filterValues.get(1);
						LocalDate time = ((DateValue) valueTask).getValue();
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
		return value.getAttType() == AttributeType.DATE || value.getAttType() == null;
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return new NoValue();
	}

}
