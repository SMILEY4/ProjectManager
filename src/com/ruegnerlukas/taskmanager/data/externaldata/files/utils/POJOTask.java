package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

import java.util.HashMap;

public class POJOTask {


	public HashMap<Integer, TaskValue<?>> values;




	public POJOTask(Task task) {
		this.values = new HashMap<>();
		for (TaskAttribute key : task.values.keySet()) {
			this.values.put(key.id, task.values.get(key));
		}
	}

}
