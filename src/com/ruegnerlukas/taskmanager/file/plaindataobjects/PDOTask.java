package com.ruegnerlukas.taskmanager.file.plaindataobjects;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDOTask {


	public static PDOTask fromTask(Task task) {
		PDOTask pdo = new PDOTask();
		pdo.values = new HashMap<>();
		for (Map.Entry<TaskAttribute, TaskValue<?>> entry : task.values.entrySet()) {
			pdo.values.put(entry.getKey().name.get(), entry.getValue());
		}
		return pdo;
	}




	public static Task toTask(PDOTask pdo, List<TaskAttribute> attributes) {
		Task task = new Task();
		for (Map.Entry<String, TaskValue<?>> entry : pdo.values.entrySet()) {
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
