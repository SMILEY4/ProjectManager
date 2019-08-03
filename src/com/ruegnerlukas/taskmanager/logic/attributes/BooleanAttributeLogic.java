package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.BoolValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class BooleanAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<Boolean> COMPARATOR_ASC = Boolean::compare;
	private final Comparator<Boolean> COMPARATOR_DESC = (x, y) -> Boolean.compare(x, y) * -1;




	protected BooleanAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Boolean.class});
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
		setDefaultValue(attribute, new BoolValue(false));
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




	public void setDefaultValue(TaskAttributeData attribute, BoolValue defaultValue) {
		attribute.getValues().put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public BoolValue getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (BoolValue) value.getValue();
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return filterValues.get(0) == ((BoolValue) valueTask).getValue();
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return filterValues.get(0) != ((BoolValue) valueTask).getValue();
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
		return value.getAttType() == null || value.getAttType() == AttributeType.BOOLEAN;
	}




	@Override
	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttributeData attribute, boolean preferNoValue) {
		return preferNoValue ? new NoValue() : new BoolValue(false);
	}


}
