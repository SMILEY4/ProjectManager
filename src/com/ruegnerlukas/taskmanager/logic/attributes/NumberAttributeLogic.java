package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NumberValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class NumberAttributeLogic {


	public static final String NUMBER_DEC_PLACES = "number_dec_places";
	public static final String NUMBER_MIN_VALUE = "number_min_value";
	public static final String NUMBER_MAX_VALUE = "number_max_value";

	public static final Map<String, Class<?>> DATA_TYPES;
	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Double> COMPARATOR_ASC = Double::compareTo;
	public static final Comparator<Double> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
		Map<String, Class<?>> mapTypes = new HashMap<>();
		mapTypes.put(NUMBER_DEC_PLACES, Integer.class);
		mapTypes.put(NUMBER_MIN_VALUE, Double.class);
		mapTypes.put(NUMBER_MAX_VALUE, Double.class);
		mapTypes.put(TaskAttribute.ATTRIB_USE_DEFAULT, Boolean.class);
		mapTypes.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, NumberValue.class);
		mapTypes.put(TaskAttribute.ATTRIB_TASK_VALUE_TYPE, NumberValue.class);
		DATA_TYPES = Collections.unmodifiableMap(mapTypes);

		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{Double.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{Double.class, Double.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{Double.class, Double.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




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
		setDefaultValue(attribute, new NumberValue(0));
	}




	public static void setDecPlaces(TaskAttribute attribute, int decPlaces) {
		attribute.values.put(NUMBER_DEC_PLACES, decPlaces);
	}




	public static int getDecPlaces(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_DEC_PLACES);
	}




	public static void setMinValue(TaskAttribute attribute, int minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, (double) minValue);
	}




	public static void setMinValue(TaskAttribute attribute, double minValue) {
		attribute.values.put(NUMBER_MIN_VALUE, minValue);
	}




	public static Number getMinValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MIN_VALUE);
	}




	public static void setMaxValue(TaskAttribute attribute, int maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, (double) maxValue);
	}




	public static void setMaxValue(TaskAttribute attribute, double maxValue) {
		attribute.values.put(NUMBER_MAX_VALUE, maxValue);
	}




	public static Number getMaxValue(TaskAttribute attribute) {
		return attribute.getValue(NUMBER_MAX_VALUE);
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(TaskAttribute.ATTRIB_USE_DEFAULT, useDefault);
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_USE_DEFAULT);
	}




	public static void setDefaultValue(TaskAttribute attribute, NumberValue defaultValue) {
		attribute.values.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, defaultValue);
	}




	public static NumberValue getDefaultValue(TaskAttribute attribute) {
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((NumberValue) valueTask).getValue().compareTo((Double) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Double && filterValues.get(1) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						double min = (Double) filterValues.get(0);
						double max = (Double) filterValues.get(1);
						double val = ((NumberValue) valueTask).getValue();
						return min <= val && max >= val;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Double && filterValues.get(1) instanceof Double) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						double min = (Double) filterValues.get(0);
						double max = (Double) filterValues.get(1);
						double val = ((NumberValue) valueTask).getValue();
						return !(min <= val && max >= val);
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
		if (value.getAttType() == AttributeType.NUMBER) {
			final double number = ((NumberValue) value).getValue();
			final double min = getMinValue(attribute).doubleValue();
			final double max = getMaxValue(attribute).doubleValue();
			return min <= number && number <= max;
		} else {
			return value.getAttType() == null;
		}
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		if (preferNoValue) {
			return new NoValue();
		} else {
			if (getUseDefault(attribute)) {
				return getDefaultValue(attribute);
			} else {
				final double number = (oldValue.getAttType() != AttributeType.NUMBER) ? 0.0 : ((NumberValue) oldValue).getValue();
				final double min = getMinValue(attribute).doubleValue();
				final double max = getMaxValue(attribute).doubleValue();
				if (number < min) {
					return new NumberValue(min);
				} else {
					return new NumberValue(max);
				}
			}
		}
	}

}
