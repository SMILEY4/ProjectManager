package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.lang.reflect.Field;
import java.util.Map;

public class TaskLogic {


	public static Task createTask() {
		// TODO
		return new Task();
	}




	public static boolean setValue(Task task, TaskAttribute attribute, Object value) {

		// validate value-type
		try {
			Field field = AttributeLogic.LOGIC_CLASSED.get(attribute.type.get()).getField("DATA_TYPES");
			Map<String, Class<?>> map = (Map<String, Class<?>>) field.get(null);
			if (value.getClass() != map.get("task_value")) {
				return false;
			}
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		// TODO: valudate value

		task.attributes.put(attribute, value);
		return true;
	}


}
