package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.*;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogicManager;

import java.time.LocalDate;
import java.util.Map;

public class TaskLogic {


	public static Task createTask(Project project) {
		Task task = new Task();

		LocalDate time = LocalDate.now();

		// set id
		TaskAttribute idAttribute = AttributeLogic.findAttribute(project, AttributeType.ID);
		final int id = project.settings.idCounter.get();
		project.settings.idCounter.set(id + 1);
		setValue(task, idAttribute, id);

		// set date created
		TaskAttribute createdAttribute = AttributeLogic.findAttribute(project, AttributeType.CREATED);
		setValue(task, createdAttribute, time);

		// set last updated
		TaskAttribute updatedAttribute = AttributeLogic.findAttribute(project, AttributeType.LAST_UPDATED);
		setValue(task, updatedAttribute, time);

		return task;
	}




	public static <T> T getValue(Task task, TaskAttribute attribute, Class<T> type) {
		Object trueValue = getTrueValue(task, attribute);
		if (trueValue == null || trueValue instanceof NoValue) {
			if (AttributeLogic.getUsesDefault(attribute)) {
				return (T) AttributeLogic.getDefaultValue(attribute);
			} else {
				return null;
			}
		} else {
			return (T) trueValue;
		}
	}




	public static Object getValue(Task task, TaskAttribute attribute) {
		Object trueValue = getTrueValue(task, attribute);
		if (trueValue == null || trueValue instanceof NoValue) {
			if (AttributeLogic.getUsesDefault(attribute)) {
				return AttributeLogic.getDefaultValue(attribute);
			} else {
				return new NoValue();
			}
		} else {
			return trueValue;
		}
	}




	public static <T> T getTrueValue(Task task, TaskAttribute attribute, Class<T> type) {
		Object value = getTrueValue(task, attribute);
		if (value == null || value instanceof NoValue || value.getClass() != type) {
			return null;
		} else {
			return (T) value;
		}
	}




	public static Object getTrueValue(Task task, TaskAttribute attribute) {
		return task.attributes.get(attribute);
	}




	public static boolean setValue(Task task, TaskAttribute attribute, Object value) {

		// validate type
		Map<String, Class<?>> map = AttributeLogicManager.getDataTypeMap(attribute.type.get());
		if (value.getClass() != map.get(AttributeLogic.ATTRIB_TASK_VALUE_TYPE)) {
			return false;
		}

		// TODO: validate value

		task.attributes.put(attribute, value);
		return true;
	}


}
