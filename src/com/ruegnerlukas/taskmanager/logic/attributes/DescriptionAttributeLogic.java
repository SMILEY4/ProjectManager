package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DescriptionValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class DescriptionAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<String> COMPARATOR_ASC = String::compareTo;
	private final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected DescriptionAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.EQUALS_IGNORE_CASE, new Class<?>[]{String.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.CONTAINS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.CONTAINS_NOT, new Class<?>[]{String.class});
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
		setUseDefault(attribute, true);
		setDefaultValue(attribute, new DescriptionValue(""));
	}




	private void setUseDefault(TaskAttributeData attribute, boolean useDefault) {
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




	private void setDefaultValue(TaskAttributeData attribute, DescriptionValue defaultValue) {
		attribute.getValues().put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public DescriptionValue getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (DescriptionValue) value.getValue();
		}
	}




	@Override
	public boolean matchesFilter(TaskData task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute);
		List<Object> filterValues = criteria.values;

		switch (criteria.operation) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().equals(filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case EQUALS_IGNORE_CASE: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().equalsIgnoreCase((String) filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return !((DescriptionValue) valueTask).getValue().equals(filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case CONTAINS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((DescriptionValue) valueTask).getValue().contains((String) filterValues.get(0));
					}
				} else {
					return false;
				}
			}

			case CONTAINS_NOT: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return !((DescriptionValue) valueTask).getValue().contains((String) filterValues.get(0));
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
		return value.getAttType() == AttributeType.DESCRIPTION;
	}




	@Override
	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttributeData attribute, boolean preferNoValue) {
		return null;
	}

}