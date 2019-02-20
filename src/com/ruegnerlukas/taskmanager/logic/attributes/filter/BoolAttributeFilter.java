package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class BoolAttributeFilter extends AttributeFilter {


	public static final FilterCriteria.ComparisonOp[] CMP_OPS = new FilterCriteria.ComparisonOp[]{
			FilterCriteria.ComparisonOp.EQUALITY,
			FilterCriteria.ComparisonOp.INEQUALITY
	};




	@Override
	protected boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue) {
		switch (cmp) {
			case EQUALITY:
				return matchEquality(taskValue, filterValue);
			case INEQUALITY:
				return matchInequality(taskValue, filterValue);
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

}
