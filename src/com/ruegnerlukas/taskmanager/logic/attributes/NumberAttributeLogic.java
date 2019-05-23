package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NumberValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class NumberAttributeLogic {


	public static final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	public static final Comparator<Double> COMPARATOR_ASC = Double::compareTo;
	public static final Comparator<Double> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	static {
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
		attribute.values.put(AttributeValueType.NUMBER_DEC_PLACES, new NumberDecPlacesValue(decPlaces));
	}




	public static int getDecPlaces(TaskAttribute attribute) {
		NumberDecPlacesValue value = (NumberDecPlacesValue) attribute.getValue(AttributeValueType.NUMBER_DEC_PLACES);
		if(value == null) {
			return 0;
		} else {
			return value.getValue();
		}

	}




	public static void setMinValue(TaskAttribute attribute, int minValue) {
		attribute.values.put(AttributeValueType.NUMBER_MIN, new NumberMinValue(minValue));
	}




	public static void setMinValue(TaskAttribute attribute, double minValue) {
		attribute.values.put(AttributeValueType.NUMBER_MIN, new NumberMinValue(minValue));
	}




	public static Number getMinValue(TaskAttribute attribute) {
		NumberMinValue value = (NumberMinValue) attribute.getValue(AttributeValueType.NUMBER_MIN);
		if(value == null) {
			return (double) Integer.MIN_VALUE;
		} else {
			return value.getValue();
		}
	}




	public static void setMaxValue(TaskAttribute attribute, int maxValue) {
		attribute.values.put(AttributeValueType.NUMBER_MAX, new NumberMaxValue(maxValue));
	}




	public static void setMaxValue(TaskAttribute attribute, double maxValue) {
		attribute.values.put(AttributeValueType.NUMBER_MAX, new NumberMaxValue(maxValue));
	}




	public static Number getMaxValue(TaskAttribute attribute) {
		NumberMaxValue value = (NumberMaxValue) attribute.getValue(AttributeValueType.NUMBER_MAX);
		if(value == null) {
			return (double) Integer.MAX_VALUE;
		} else {
			return value.getValue();
		}
	}




	public static void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(useDefault));
	}




	public static boolean getUseDefault(TaskAttribute attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if(value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	public static void setDefaultValue(TaskAttribute attribute, NumberValue defaultValue) {
		attribute.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public static NumberValue getDefaultValue(TaskAttribute attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if(value == null) {
			return null;
		} else {
			return (NumberValue) value.getValue();
		}
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
