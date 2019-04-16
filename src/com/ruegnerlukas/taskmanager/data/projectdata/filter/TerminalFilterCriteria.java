package com.ruegnerlukas.taskmanager.data.projectdata.filter;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.observables.TaskAttributeProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TerminalFilterCriteria extends FilterCriteria {


	public TaskAttributeProperty attribute = new TaskAttributeProperty();
	public FilterOperation operation; // TODO  -> property
	public ObservableList<Object> values = FXCollections.observableArrayList();




	public TerminalFilterCriteria(TaskAttribute attribute, FilterOperation operation, Object... values) {
		super(CriteriaType.TERMINAL);
		this.attribute.set(attribute);
		this.operation = operation;
		this.values.addAll(values);
	}

}
