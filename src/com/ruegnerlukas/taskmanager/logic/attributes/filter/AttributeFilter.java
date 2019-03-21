package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;
import com.ruegnerlukas.taskmanager.logic.Logic;

public abstract class AttributeFilter {


	public static FilterCriteria.ComparisonOp[] getPossibleComparisionOps(TaskAttribute attribute) {
		TaskAttributeType type = attribute.data.getType();
		if (type == TaskAttributeType.BOOLEAN) {
			return BoolAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.CHOICE) {
			return ChoiceAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.DESCRIPTION) {
			return DescriptionAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.FLAG) {
			return FlagAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.ID) {
			return IDAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.NUMBER) {
			return NumberAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.TEXT) {
			return TextAttributeFilter.CMP_OPS;
		}
		if (type == TaskAttributeType.DEPENDENCY) {
			return DependencyAttributeFilter.CMP_OPS;
		}
		return new FilterCriteria.ComparisonOp[]{};
	}




	public boolean match(Task task, FilterCriteria filter) {
		TaskAttributeValue taskValue = getTaskValue(filter, task);
		if (taskValue instanceof NoValue) {
			return false;
		} else {
			return match(task, getTaskValue(filter, task), getComparisonOp(filter), getFilterValue(filter), filter);
		}
	}




	abstract boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue, FilterCriteria filterCriteria);




	static TaskAttribute getAttribute(FilterCriteria filter) {
		return filter.attribute;
	}




	static TaskAttributeType getAttributeType(FilterCriteria filter) {
		return filter.attribute.data.getType();
	}




	static FilterCriteria.ComparisonOp getComparisonOp(FilterCriteria filter) {
		return filter.comparisonOp;
	}




	static TaskAttributeValue getFilterValue(FilterCriteria filter) {
		return filter.comparisionValue;
	}




	static TaskAttributeValue getTaskValue(FilterCriteria filter, Task task) {
		Response<TaskAttributeValue> response = Logic.tasks.getAttributeValue(task, filter.attribute.name);
		if (response.getState() == Response.State.SUCCESS) {
			return response.getValue();
		} else {
			return null;
		}
	}




	static boolean valueInTextArray(TextArrayValue array, TextValue value) {
		for (String listElement : array.getText()) {
			if (listElement.equals(value.getText())) {
				return true;
			}
		}
		return false;
	}




	static boolean valueInTextArray(TextArrayValue array, NumberValue value) {
		for (String listElement : array.getText()) {
			if (value.isInt() && MathUtils.isInteger(listElement)) {
				if (value.getInt() == Integer.parseInt(listElement)) {
					return true;
				}
			} else if (MathUtils.isNumber(listElement)) {
				int dec = listElement.contains(".") ? listElement.split(".")[1].length() : 0;
				if (MathUtils.isNearlyEqual(Double.parseDouble(listElement), value.getDouble(), Math.pow(10, dec))) {
					return true;
				}
			}
		}
		return false;
	}


}
