package com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TerminalFilterCriteria extends FilterCriteria {


	public CustomProperty<TaskAttribute> attribute = new CustomProperty<>();
	public CustomProperty<FilterOperation> operation = new CustomProperty<>();
	public ObservableList<Object> values = FXCollections.observableArrayList(); // TODO: check if possible to change to "TaskValue"




	public TerminalFilterCriteria(TaskAttribute attribute, FilterOperation operation, Object... values) {
		super(CriteriaType.TERMINAL);
		this.attribute.set(attribute);
		this.operation.set(operation);
		this.values.addAll(values);
	}

}
