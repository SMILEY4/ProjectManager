package com.ruegnerlukas.taskmanager.logic.attributes.filter;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;

import java.util.List;

public class DependencyAttributeFilter extends AttributeFilter {


	public static final FilterCriteria.ComparisonOp[] CMP_OPS = new FilterCriteria.ComparisonOp[]{
			FilterCriteria.ComparisonOp.IS_DEPENDENT_ON_FILTER,
			FilterCriteria.ComparisonOp.IS_PREREQUISITE_OF_FILTER,
			FilterCriteria.ComparisonOp.IS_INDEPENDENT,
	};




	@Override
	protected boolean match(Task task, TaskAttributeValue taskValue, FilterCriteria.ComparisonOp cmp, TaskAttributeValue filterValue, FilterCriteria filter) {
		switch (cmp) {
			case IS_DEPENDENT_ON_FILTER:
				return matchDependentOn(task, taskValue, filterValue, filter.attribute);
			case IS_PREREQUISITE_OF_FILTER:
				return matchPrerequisiteOf(task, filterValue, filter.attribute);
			case IS_INDEPENDENT:
				return matchIndependent(taskValue, filterValue);
			default:
				return false;
		}
	}




	private boolean matchDependentOn(Task task, TaskAttributeValue taskValue, TaskAttributeValue filterValue, TaskAttribute attribute) {
		/*
		 * filterValue is list of tasks: "F = {T-5, T-61, T-18}"
		 * match if task is dependent on any task in "F" -> "taskValue" contains element that is also in "F"
		 * => e.g.: filter Task T-34 > T-34 depends on T-5 > match!
		 * */
		final Task[] tasks = ((TaskArrayValue) filterValue).getTasks();
		List<Task> list = Logic.dependencies.getPrerequisitesOf(task, attribute).getValue(); // task depends on all in list
		for (Task t : tasks) {
			if (list.contains(t)) {
				return true;
			}
		}
		return false;
	}




	private boolean matchPrerequisiteOf(Task task, TaskAttributeValue filterValue, TaskAttribute attribute) {
		/*
		 * filterValue is list of tasks: "F = {T-5, T-61, T-18}"
		 * match if any task in "F" is dependent on "task"
		 * => e.g.: filter Task T-34 > T-61 depends on T-34 > match!
		 * */
		final Task[] tasks = ((TaskArrayValue) filterValue).getTasks();
		for (Task t : tasks) {
			List<Task> list = Logic.dependencies.getPrerequisitesOf(t, attribute).getValue(); // t depends on all in list
			if (list.contains(task)) {
				return true;
			}
		}
		return false;
	}




	private boolean matchIndependent(TaskAttributeValue taskValue, TaskAttributeValue filterValue) {
		final boolean independent = ((BoolValue) filterValue).getBoolValue();
		final TaskArrayValue dependsOn = (TaskArrayValue) taskValue;
		if (independent) {
			return dependsOn.getTasks().length == 0;
		} else {
			return dependsOn.getTasks().length != 0;
		}
	}


}
