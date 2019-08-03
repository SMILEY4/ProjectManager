package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.List;

public class TaskData implements ITask {


	private ObservableMap<TaskAttributeData, TaskValue<?>> values;




	public TaskData(int id, Project project) {
		this.values = FXCollections.observableHashMap();
		TaskAttribute idAttribute = AttributeLogic.findAttributeByType(project, AttributeType.ID);
		values.put(idAttribute, new IDValue(id));
	}




	@Override
	public TaskValue<?> getValue(TaskAttributeData attribute) {
		return values.get(attribute);
	}




	@Override
	public ObservableMap<TaskAttributeData, TaskValue<?>> getValues() {
		return this.values;
	}




	@Override
	public List<TaskValue<?>> getValueList() {
		return new ArrayList<>(this.values.values());
	}


}
