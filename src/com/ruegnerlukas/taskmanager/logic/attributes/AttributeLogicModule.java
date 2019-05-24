package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;

import java.util.Comparator;
import java.util.Map;

public interface AttributeLogicModule {


	Map<FilterOperation, Class<?>[]> getFilterData();

	default Comparator getComparator(SortElement.SortDir dir) {
		if (dir == SortElement.SortDir.ASC) {
			return getComparatorAsc();
		} else {
			return getComparatorDesc();
		}
	}

	Comparator getComparatorAsc();

	Comparator getComparatorDesc();

	TaskAttribute createAttribute();

	TaskAttribute createAttribute(String name);

	void initAttribute(TaskAttribute attribute);

	boolean matchesFilter(Task task, TerminalFilterCriteria criteria);

	boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value);

	TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue);


}

