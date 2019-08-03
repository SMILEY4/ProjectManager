package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DateValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.LocalDate;
import java.util.*;

public class DateAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<LocalDate> COMPARATOR_ASC = LocalDate::compareTo;
	private final Comparator<LocalDate> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected DateAttributeLogic() {
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




	@Override
	public Map<FilterOperation, Class<?>[]> getFilterData() {
		return FILTER_DATA;
	}




	@Override
	public Comparator getComparatorAsc() {
		return COMPARATOR_ASC;
	}




	@Override
	public Comparator getComparatorDesc() {
		return COMPARATOR_DESC;
	}




	@Override
	public void initAttribute(TaskAttributeData attribute) {
		attribute.getValues().clear();
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new DateValue(LocalDate.now()));
	}




	public void setUseDefault(TaskAttributeData attribute, boolean useDefault) {
		attribute.getValues().put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(useDefault));
	}




	public boolean getUseDefault(TaskAttributeData attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	public void setDefaultValue(TaskAttributeData attribute, DateValue defaultValue) {
		attribute.getValues().put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public DateValue getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (DateValue) value.getValue();
		}
	}




	@Override
	public boolean matchesFilter(TaskData task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute);
		List<Object> filterValues = criteria.values;

		switch (criteria.operation) {

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




	@Override
	public boolean isValidTaskValue(TaskAttributeData attribute, TaskValue<?> value) {
		return value.getAttType() == AttributeType.DATE || value.getAttType() == null;
	}




	@Override
	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttributeData attribute, boolean preferNoValue) {
		return oldValue.getAttType() == AttributeType.DATE ? new DateValue((LocalDate) oldValue.getValue())
				: (preferNoValue ? new NoValue() : new DateValue(LocalDate.now()));
	}

}
