package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.util.HashMap;

public class POJOTask {


	public int id;
	public HashMap<Integer, TaskValue<?>> values;




	public POJOTask(Task task) {
		this.values = new HashMap<>();
		for (TaskAttribute key : task.values.keySet()) {
			if (key.type.get() == AttributeType.ID) {
				IDValue idValue = (IDValue) task.values.get(key);
				this.id = idValue.getValue();
			} else {
				this.values.put(key.id, task.values.get(key));
			}
		}
	}




	public Task toTask(Project project) {
		Task task = new Task(id, project, project.dataHandler);
		for (int attID : values.keySet()) {
			TaskAttribute attribute = AttributeLogic.findAttributeByID(project, attID);
			if (attribute != null) {
				task.values.put(attribute, values.get(attID));
			}
		}
		return task;
	}

}
