package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.TextMultilineValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TextValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class TextAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<String> COMPARATOR_ASC = String::compareTo;
	private final Comparator<String> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected TextAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.HAS_VALUE, new Class<?>[]{Boolean.class});
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
		setMultiline(attribute, false);
		setUseDefault(attribute, false);
		setDefaultValue(attribute, new TextValue(""));
	}




	public void setMultiline(TaskAttributeData attribute, boolean multiline) {
		attribute.getValues().put(AttributeValueType.TEXT_MULTILINE, new TextMultilineValue(multiline));
	}




	public boolean getMultiline(TaskAttributeData attribute) {
		TextMultilineValue value = (TextMultilineValue) attribute.getValue(AttributeValueType.TEXT_MULTILINE);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	public void setUseDefault(TaskAttributeData attribute, boolean useDefault) {
		attribute.getValues().put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(useDefault));
	}




	public boolean getUseDefault(TaskAttribute attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	public void setDefaultValue(TaskAttributeData attribute, TextValue defaultValue) {
		attribute.getValues().put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public TextValue getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (TextValue) value.getValue();
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
						return ((TextValue) valueTask).getValue().equals(filterValues.get(0));
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
						return ((TextValue) valueTask).getValue().equalsIgnoreCase((String) filterValues.get(0));
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
						return !((TextValue) valueTask).getValue().equals(filterValues.get(0));
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
						return ((TextValue) valueTask).getValue().contains((String) filterValues.get(0));
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
						return !((TextValue) valueTask).getValue().contains((String) filterValues.get(0));
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
		if (value.getAttType() == AttributeType.TEXT) {
			return true;
		} else {
			return value.getAttType() == null;
		}
	}




	@Override
	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttributeData attribute, boolean preferNoValue) {
		if (preferNoValue) {
			return new NoValue();
		} else {
			final String value = (oldValue.getAttType() == null) ? "" : ((TextValue) oldValue).getValue();
			return new TextValue(value);
		}
	}

}
