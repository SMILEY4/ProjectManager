package com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TerminalFilterCriteria extends FilterCriteria {


	public final TaskAttribute attribute;
	public final FilterOperation operation;
	public List<Object> values;




	public TerminalFilterCriteria(TaskAttribute attribute, FilterOperation operation, Object... values) {
		super(CriteriaType.TERMINAL);
		this.attribute = attribute;
		this.operation = operation;
		this.values = Collections.unmodifiableList(Arrays.asList(values));
	}

}
