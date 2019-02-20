package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;

public class ChoiceAttributeFilter extends AttributeFilter {


	public static final FilterCriteria.ComparisonOp[] CMP_OPS = new FilterCriteria.ComparisonOp[]{
			FilterCriteria.ComparisonOp.EQUALITY,
			FilterCriteria.ComparisonOp.INEQUALITY,
			FilterCriteria.ComparisonOp.IN_LIST,
			FilterCriteria.ComparisonOp.NOT_IN_LIST,
			FilterCriteria.ComparisonOp.CONTAINS,
			FilterCriteria.ComparisonOp.CONTAINS_NOT
	};




	@Override
	protected boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue) {
		switch (cmp) {
			case EQUALITY:
				return matchEquality(taskValue, filterValue);
			case INEQUALITY:
				return matchInequality(taskValue, filterValue);
			case IN_LIST:
				return matchInList(taskValue, filterValue);
			case NOT_IN_LIST:
				return matchNotInList(taskValue, filterValue);
			case CONTAINS:
				return matchContains(taskValue, filterValue);
			case CONTAINS_NOT:
				return matchContainsNot(taskValue, filterValue);
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
		return valueInTextArray((TextArrayValue) taskValue, (TextValue) filterValue);
	}




	private boolean matchNotInList(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		return !valueInTextArray((TextArrayValue) taskValue, (TextValue) filterValue);
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
