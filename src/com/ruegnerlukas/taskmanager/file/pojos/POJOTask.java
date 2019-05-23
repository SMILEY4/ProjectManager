package com.ruegnerlukas.taskmanager.file.pojos;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POJOTask {


	public static POJOTask fromTask(Task task) {
		POJOTask pojo = new POJOTask();
		pojo.values = new HashMap<>();
		for (Map.Entry<TaskAttribute, TaskValue<?>> entry : task.values.entrySet()) {
			pojo.values.put(entry.getKey().name.get(), entry.getValue());
		}
		return pojo;
	}




	public static Task toTask(POJOTask pojo, List<TaskAttribute> attributes) {
		Task task = new Task();
		for (Map.Entry<String, TaskValue<?>> entry : pojo.values.entrySet()) {
			TaskAttribute attribute = null;
			for (TaskAttribute att : attributes) {
				if (att.name.get().equals(entry.getKey())) {
					attribute = att;
					break;
				}
			}
			if (attribute != null) {
				task.values.put(attribute, entry.getValue());
			}
		}
		return task;
	}




	public Map<String, TaskValue<?>> values;


}
