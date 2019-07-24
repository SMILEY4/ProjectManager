package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

import java.util.Comparator;
import java.util.Map;

public interface AttributeLogicModule {


	/**
	 * @return the valid filter operations and parameter (amount and type) for the {@link AttributeType} of this LogicModule.
	 */
	Map<FilterOperation, Class<?>[]> getFilterData();


	/**
	 * @return a valid comparator (for the given {@link SortElement.SortDir}) for comparing raw task-values.
	 */
	default Comparator getComparator(SortElement.SortDir dir) {
		if (dir == SortElement.SortDir.ASC) {
			return getComparatorAsc();
		} else {
			return getComparatorDesc();
		}
	}


	/**
	 * @return a valid comparator (ascending order) for comparing raw task-values.
	 */
	Comparator getComparatorAsc();


	/**
	 * @return a valid comparator (descending order) for comparing raw task-values.
	 */
	Comparator getComparatorDesc();


	/**
	 * Initializes the given {@link TaskAttribute} as a attribute of the {@link AttributeType} of this LogicModule.
	 * This will completely reset the attribute to its default state.
	 */
	void initAttribute(TaskAttribute attribute);


	/**
	 * @return true, if the values of the given {@link Task} match the specified values of the given {@link TerminalFilterCriteria}.
	 */
	boolean matchesFilter(Task task, TerminalFilterCriteria criteria);


	/**
	 * @return true, if the given {@link TaskValue} is valid for the given {@link TaskAttribute}.
	 */
	boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value);


	/**
	 * @return a new valid {@link TaskValue} similar to the given old value or {@link NoValue}
	 * (depdending on the value of "preferNoValue", the {@link AttributeType} and the given attribute).
	 */
	TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue);


}

