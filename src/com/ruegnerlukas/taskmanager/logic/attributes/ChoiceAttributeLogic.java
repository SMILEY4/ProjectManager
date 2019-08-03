package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.ChoiceListValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.ChoiceValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class ChoiceAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<String> COMPARATOR_ASC = String::compareTo;
	private final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected ChoiceAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{String.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{String.class});
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
		setValueList(attribute, new String[]{});
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new ChoiceValue(""));
	}




	public void addValueToList(TaskAttributeData attribute, String value) {
		if (!containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = Arrays.copyOf(values, values.length + 1);
			newValues[newValues.length - 1] = value;
			setValueList(attribute, newValues);
		}
	}




	public void removeValueFromList(TaskAttributeData attribute, String value) {
		if (containsValue(attribute, value)) {
			String[] values = getValueList(attribute);
			String[] newValues = new String[values.length - 1];
			for (int i = 0, j = 0; i < values.length; i++) {
				if (!values[i].equals(value)) {
					newValues[j++] = values[i];
				}
			}
			setValueList(attribute, newValues);
		}
	}




	public void setValueList(TaskAttributeData attribute, List<String> valueList) {
		String[] array = new String[valueList.size()];
		for (int i = 0; i < valueList.size(); i++) {
			array[i] = valueList.get(i);
		}
		setValueList(attribute, array);
	}




	public void setValueList(TaskAttributeData attribute, String... valueList) {
		attribute.getValues().put(AttributeValueType.CHOICE_VALUES, new ChoiceListValue(valueList));
	}




	public String[] getValueList(TaskAttributeData attribute) {
		ChoiceListValue value = (ChoiceListValue) attribute.getValue(AttributeValueType.CHOICE_VALUES);
		if (value == null) {
			return new String[]{};
		} else {
			return value.getValue();
		}
	}




	public boolean containsValue(TaskAttributeData attribute, String value) {
		String[] values = getValueList(attribute);
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				return true;
			}
		}
		return false;
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




	public void setDefaultValue(TaskAttributeData attribute, ChoiceValue defaultValue) {
		attribute.getValues().put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public ChoiceValue getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (ChoiceValue) value.getValue();
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
				if (filterValues.size() == 1 && filterValues.get(0) instanceof String) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((String) filterValues.get(0)).equalsIgnoreCase(((ChoiceValue) valueTask).getValue());
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
						return !((String) filterValues.get(0)).equalsIgnoreCase(((ChoiceValue) valueTask).getValue());
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
		if (value.getAttType() == null) {
			return true;
		} else if (value.getAttType() == AttributeType.CHOICE) {
			return ArrayUtils.contains(getValueList(attribute), ((ChoiceValue) value).getValue());
		} else {
			return false;
		}
	}




	@Override
	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttributeData attribute, boolean preferNoValue) {
		if (preferNoValue || getValueList(attribute).length == 0) {
			return new NoValue();
		} else {
			return new ChoiceValue(getValueList(attribute)[0]);
		}
	}

}