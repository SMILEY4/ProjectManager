package com.ruegnerlukas.taskmanager.data.projectdata.filter;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TerminalFilterCriteria extends FilterCriteria {


	public CustomProperty<TaskAttribute> attribute = new CustomProperty<>();
	public CustomProperty<FilterOperation> operation = new CustomProperty<>();
	public ObservableList<Object> values = FXCollections.observableArrayList();




	public TerminalFilterCriteria(TaskAttribute attribute, FilterOperation operation, Object... values) {
		super(CriteriaType.TERMINAL);
		this.attribute.set(attribute);
		this.operation.set(operation);
		this.values.addAll(values);
	}

}
