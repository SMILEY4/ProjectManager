package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class TextAttributeFilter extends AttributeFilter {


	public static final FilterCriteria.ComparisonOp[] CMP_OPS = new FilterCriteria.ComparisonOp[]{
			FilterCriteria.ComparisonOp.EQUALITY,
			FilterCriteria.ComparisonOp.INEQUALITY,
			FilterCriteria.ComparisonOp.CONTAINS,
			FilterCriteria.ComparisonOp.CONTAINS_NOT,
	};




	@Override
	protected boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue) {
		switch (cmp) {
			case CONTAINS:
				return matchContains(taskValue, filterValue);
			case CONTAINS_NOT:
				return matchContainsNot(taskValue, filterValue);
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




	private boolean matchContains(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		String strTask = ((TextValue) taskValue).getText();
		String strFilter = ((TextValue) filterValue).getText();
		return strTask.contains(strFilter);
	}




	private boolean matchContainsNot(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		String strTask = ((TextValue) taskValue).getText();
		String strFilter = ((TextValue) filterValue).getText();
		return !strTask.contains(strFilter);
	}


}
