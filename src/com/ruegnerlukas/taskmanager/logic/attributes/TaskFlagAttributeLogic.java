package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.FlagListValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class TaskFlagAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<TaskFlag> COMPARATOR_ASC = Comparator.comparing(x -> x.name.get());
	private final Comparator<TaskFlag> COMPARATOR_DESC = (x, y) -> x.name.get().compareTo(y.name.get()) * -1;




	protected TaskFlagAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{TaskFlag.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{TaskFlag.class});
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




	public TaskAttribute createAttribute() {
		return createAttribute("FlagAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.FLAG);
		this.initAttribute(attribute);
		return attribute;
	}




	public void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
		TaskFlag defaultFlag = new TaskFlag("Default", TaskFlag.FlagColor.GRAY);
		setFlagList(attribute, new TaskFlag[]{defaultFlag});
		setUseDefault(attribute, true);
		setDefaultValue(attribute, new FlagValue(defaultFlag));
	}




	public void addFlagToList(TaskAttribute attribute, TaskFlag flag) {
		if (!containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = Arrays.copyOf(list, list.length + 1);
			newList[newList.length - 1] = flag;
			setFlagList(attribute, newList);
		}
	}




	public void removeFlagFromList(TaskAttribute attribute, TaskFlag flag) {
		if (containsFlag(attribute, flag)) {
			TaskFlag[] list = getFlagList(attribute);
			TaskFlag[] newList = new TaskFlag[list.length - 1];
			for (int i = 0, j = 0; i < list.length; i++) {
				if (list[i] != flag) {
					newList[j++] = list[i];
				}
			}
			setFlagList(attribute, newList);
		}
	}




	public void setFlagList(TaskAttribute attribute, TaskFlag[] list) {
		attribute.values.put(AttributeValueType.FLAG_LIST, new FlagListValue(list));
	}




	public TaskFlag[] getFlagList(TaskAttribute attribute) {
		FlagListValue value = (FlagListValue) attribute.getValue(AttributeValueType.FLAG_LIST);
		if (value == null) {
			return new TaskFlag[]{};
		} else {
			return value.getValue();
		}
	}




	public boolean containsFlag(TaskAttribute attribute, TaskFlag flag) {
		TaskFlag[] list = getFlagList(attribute);
		for (int i = 0; i < list.length; i++) {
			if (list[i] == flag) {
				return true;
			}
		}
		return false;
	}




	private void setUseDefault(TaskAttribute attribute, boolean useDefault) {
		attribute.values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(useDefault));
	}




	public boolean getUseDefault(TaskAttribute attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	private void setDefaultValue(TaskAttribute attribute, FlagValue defaultValue) {
		attribute.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(defaultValue));
	}




	public FlagValue getDefaultValue(TaskAttribute attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return (FlagValue) value.getValue();
		}
	}




	public boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {
		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof TaskFlag) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((FlagValue) valueTask).getValue() == filterValues.get(0);
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof TaskFlag) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((FlagValue) valueTask).getValue() != filterValues.get(0);
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




	public boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value) {
		if (value.getAttType() == AttributeType.FLAG) {
			FlagValue valueFlag = (FlagValue) value;
			return ArrayUtils.contains(getFlagList(attribute), valueFlag.getValue());
		} else {
			return value.getAttType() == null;
		}
	}




	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		TaskFlag[] flags = getFlagList(attribute);
		if (flags.length == 0) {
			return getDefaultValue(attribute);
		} else {
			return new FlagValue(flags[0]);
		}
	}

}
