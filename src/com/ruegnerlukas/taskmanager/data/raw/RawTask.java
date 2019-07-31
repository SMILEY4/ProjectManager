package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.raw.taskvalues.RawTaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.HashMap;

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


}
