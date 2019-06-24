package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

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

	void initAttribute(TaskAttribute attribute);

	boolean matchesFilter(Task task, TerminalFilterCriteria criteria);

	boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value);

	TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue);


}

