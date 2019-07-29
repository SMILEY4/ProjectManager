package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

public class POJODependencyValue {


	public AttributeType type;
	public int[] value;




	public POJODependencyValue(DependencyValue value) {
		Task[] tasks = value.getValue();
		int[] ids = new int[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			ids[i] = TaskLogic.getTaskID(tasks[i]);
		}
		this.value = ids;
		this.type = AttributeType.DEPENDENCY;
	}




	public DependencyValue toValue(Project project) {
		Task[] tasks = new Task[value.length];
		for (int i = 0; i < value.length; i++) {
			tasks[i] = TaskLogic.findTaskByID(project, value[i]);
		}
		return new DependencyValue(tasks);
	}


}
