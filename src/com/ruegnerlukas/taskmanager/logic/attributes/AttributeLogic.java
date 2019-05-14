package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeLogic {


	public static final String ATTRIB_TASK_VALUE_TYPE = "attrib_task_value_type";
	public static final String ATTRIB_USE_DEFAULT = "attrib_use_default";
	public static final String ATTRIB_DEFAULT_VALUE = "attrib_default_value";




	public static TaskAttribute createTaskAttribute(AttributeType type) {
		return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()));
	}




	public static TaskAttribute createTaskAttribute(AttributeType type, String name) {
		return AttributeLogicManager.createTaskAttribute(type, name);
	}




	public static void initTaskAttribute(TaskAttribute attribute) {
		AttributeLogicManager.initTaskAttribute(attribute);
	}




	public static String renameTaskAttribute(Project project, TaskAttribute attribute, String newName) {
		for (TaskAttribute attrib : project.data.attributes) {
			if (attrib == attribute) {
				continue;
			}
			if (attrib.name.get().equalsIgnoreCase(newName)) {
				return attribute.name.get();
			}
		}
		attribute.name.set(newName);
		return attribute.name.get();
	}




	public static void setTaskAttributeType(TaskAttribute attribute, AttributeType newType) {
		AttributeLogicManager.initTaskAttribute(attribute, newType);
		attribute.type.set(newType);
	}




	public static boolean setAttributeValue(Project project, TaskAttribute attribute, String key, Object newValue, boolean preferNoValueTask) {

		// validate value
		Map<String, Class<?>> map = AttributeLogicManager.getDataTypeMap(attribute.type.get());
		if (newValue.getClass() != map.get(key)) {
			Logger.get().debug("Failed to set attribute value: " + key + "  -  wrong datatype. "
					+ "Expected: " + map.get(key).getSimpleName() + ", Actual: " + newValue.getClass().getSimpleName());
			return false;
		}

		// set value
		Object prevValue = attribute.values.get(key);
		attribute.values.put(key, newValue);
		onAttributeValueChanged(attribute, key, prevValue, newValue);

		// check tasks for changes
		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskValue<?> value = TaskLogic.getValueOrDefault(task, attribute);
			if (!AttributeLogicManager.isValidTaskValue(attribute, value)) {
				TaskValue<?> validValue = AttributeLogicManager.generateValidTaskValue(value, attribute, preferNoValueTask);
				TaskLogic.setValue(project, task, attribute, validValue);
			}
		}

		return true;
	}




	private static final List<EventHandler<AttributeValueChangeEvent>> valueChangedHandlers = new ArrayList<>();




	public static void addOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.add(handler);
	}




	public static void removeOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.remove(handler);
	}




	private static void onAttributeValueChanged(TaskAttribute attribute, String key, Object prevValue, Object newValue) {
		AttributeValueChangeEvent event = new AttributeValueChangeEvent(attribute, key, prevValue, newValue);
		for (EventHandler<AttributeValueChangeEvent> handler : valueChangedHandlers) {
			handler.handle(event);
		}
	}




	public static TaskAttribute findAttribute(Project project, String name) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.name.get().equals(name)) {
				return attribute;
			}
		}
		return null;
	}




	public static TaskAttribute findAttribute(Project project, AttributeType type) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				return attribute;
			}
		}
		return null;
	}




	public static List<TaskAttribute> findAttributes(Project project, AttributeType type) {
		List<TaskAttribute> list = new ArrayList<>();
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				list.add(attribute);
			}
		}
		return list;
	}




	public static boolean getUsesDefault(TaskAttribute attribute) {
		Boolean value = attribute.getValue(AttributeLogic.ATTRIB_USE_DEFAULT);
		if (value != null) {
			return value;
		} else {
			return false;
		}
	}




	public static boolean hasDefaultValueDefined(TaskAttribute attribute) {
		return attribute.values.containsKey(AttributeLogic.ATTRIB_DEFAULT_VALUE);
	}




	public static TaskValue<?> getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(AttributeLogic.ATTRIB_DEFAULT_VALUE);
	}


}
