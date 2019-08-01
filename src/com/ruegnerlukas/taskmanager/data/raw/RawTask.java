package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.raw.taskvalues.RawTaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.util.HashMap;
import java.util.Map;

public class RawTask {


	public int id;
	public HashMap<Integer, RawTaskValue> values = new HashMap<>();




	public static RawTask toRaw(Task task) {
		RawTask raw = new RawTask();
		raw.id = TaskLogic.getTaskID(task);
		for (TaskAttribute attribute : task.values.keySet()) {
			if (attribute.type.get() != AttributeType.ID) {
				raw.values.put(attribute.id, RawTaskValue.toRaw(task.values.get(attribute)));
			}
		}
		return raw;
	}




	public static Task fromRaw(RawTask rawTask, RawProject rawProject, Project project) {
		Task task = new Task(rawTask.id, project);
		fromRaw(task, rawTask, rawProject, project);
		return task;
	}




	public static void fromRaw(Task task, RawTask rawTask, RawProject rawProject, Project project) {
		for (Map.Entry<Integer, RawTaskValue> entry : rawTask.values.entrySet()) {
			TaskAttribute attribute = AttributeLogic.findAttributeByID(project, entry.getKey());
			if (attribute != null) {
				task.values.put(attribute, RawTaskValue.fromRaw(entry.getValue(), rawProject, project));
			}
		}
	}

}
