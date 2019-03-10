package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberPairValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;

public class IDAttributeFilter extends AttributeFilter {


	public static final FilterCriteria.ComparisonOp[] CMP_OPS = new FilterCriteria.ComparisonOp[]{
			FilterCriteria.ComparisonOp.EQUALITY,
			FilterCriteria.ComparisonOp.INEQUALITY,
			FilterCriteria.ComparisonOp.IN_LIST,
			FilterCriteria.ComparisonOp.NOT_IN_LIST,
			FilterCriteria.ComparisonOp.GREATER_THAN,
			FilterCriteria.ComparisonOp.GREATER_THAN_EQUAL,
			FilterCriteria.ComparisonOp.LESS_THAN,
			FilterCriteria.ComparisonOp.LESS_THAN_EQUAL,
			FilterCriteria.ComparisonOp.IN_RANGE,
			FilterCriteria.ComparisonOp.NOT_IN_RANGE
	};




	@Override
	protected boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue, FilterCriteria filter) {
		switch (cmp) {
			case EQUALITY:
				return matchEquality(taskValue, filterValue);
			case INEQUALITY:
				return matchInequality(taskValue, filterValue);
			case IN_LIST:
				return matchInList(taskValue, filterValue);
			case NOT_IN_LIST:
				return matchNotInList(taskValue, filterValue);
			case GREATER_THAN:
				return matchGreaterThan(taskValue, filterValue);
			case GREATER_THAN_EQUAL:
				return matchGreaterEqual(taskValue, filterValue);
			case LESS_THAN:
				return matchLessThan(taskValue, filterValue);
			case LESS_THAN_EQUAL:
				return matchLessEqual(taskValue, filterValue);
			case IN_RANGE:
				return matchInRange(taskValue, filterValue);
			case NOT_IN_RANGE:
				return matchNotInRange(taskValue, filterValue);
			default:
				return false;
		}
	}




	private boolean matchEquality(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) == 0;
	}




	private boolean matchInequality(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) != 0;
	}




	private boolean matchInList(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return valueInTextArray((TextArrayValue) taskValue, (NumberValue) filterValue);
	}




	private boolean matchNotInList(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return !valueInTextArray((TextArrayValue) taskValue, (NumberValue) filterValue);
	}




	private boolean matchGreaterThan(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) == +1;
	}




	private boolean matchGreaterEqual(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) != -1;
	}




	private boolean matchLessThan(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) == -1;
	}




	private boolean matchLessEqual(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return taskValue.compareTo(filterValue) != +1;
	}




	private boolean matchInRange(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		NumberPairValue range = (NumberPairValue) filterValue;
		return range.inRange((NumberValue) taskValue);
	}




	private boolean matchNotInRange(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return !matchInRange(taskValue, filterValue);
	}


}
