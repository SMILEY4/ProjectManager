package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.data.raw.RawProject;
import com.ruegnerlukas.taskmanager.data.raw.RawTask;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

public class RawDependencyValue extends RawTaskValue {


	public int[] values;




	public static RawDependencyValue toRaw(DependencyValue value) {
		RawDependencyValue raw = new RawDependencyValue();
		raw.type = AttributeType.DEPENDENCY;
		raw.values = new int[value.getValue().length];
		for (int i = 0; i < raw.values.length; i++) {
			raw.values[i] = TaskLogic.getTaskID(value.getValue()[i]);
		}
		return raw;
	}




	public static DependencyValue fromRaw(RawDependencyValue rawValue, RawProject rawProject, Project project) {
		if (project == null) {
			return null;
		}

		TaskAttribute attribute = AttributeLogic.findAttributeByType(project, AttributeType.DEPENDENCY);
		if (attribute == null) {
			return null;
		}

		Task[] tasks = new Task[rawValue.values.length];
		for (int i = 0; i < tasks.length; i++) {
			Task t = TaskLogic.findTaskByID(project, rawValue.values[i]);

			if (t == null && rawProject != null) {
				// required task not yet added to project -> look in rawProject and add to project
				RawTask rt = null;
				for (RawTask rt0 : rawProject.tasks) {
					if (rt0.id == rawValue.values[i]) {
						rt = rt0;
						break;
					}
				}

				if (rt != null) {
					Task task = new Task(rt.id, project);
					project.data.tasks.add(task);
					RawTask.fromRaw(task, rt, rawProject, project);
					t = task;
				}

			}

			tasks[i] = t;
		}

		return new DependencyValue(tasks);

	}

}
