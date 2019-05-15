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




	public static void setTaskAttributeType(Project project, TaskAttribute attribute, AttributeType newType) {
		AttributeLogicManager.initTaskAttribute(attribute, newType);
		attribute.type.set(newType);

		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskLogic.setValue(project, task, attribute, null);
		}
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
			TaskValue<?> value = TaskLogic.getTaskValue(task, attribute);
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
		Boolean value = attribute.getValue(TaskAttribute.ATTRIB_USE_DEFAULT);
		if (value != null) {
			return value;
		} else {
			return false;
		}
	}




	public static TaskValue<?> getDefaultValue(TaskAttribute attribute) {
		return attribute.getValue(TaskAttribute.ATTRIB_DEFAULT_VALUE);
	}




	public static void setCardDisplayType(TaskAttribute attribute, TaskAttribute.CardDisplayType type) {
		TaskAttribute.CardDisplayType prev = (TaskAttribute.CardDisplayType) attribute.values.get(TaskAttribute.ATTRIB_CARD_DISPLAY_TYPE);
		attribute.values.put(TaskAttribute.ATTRIB_CARD_DISPLAY_TYPE, type);
		onAttributeValueChanged(attribute, TaskAttribute.ATTRIB_CARD_DISPLAY_TYPE, prev, type);
	}




	public static TaskAttribute.CardDisplayType getCardDisplayType(TaskAttribute attribute) {
		final TaskAttribute.CardDisplayType type = (TaskAttribute.CardDisplayType) attribute.values.get(TaskAttribute.ATTRIB_CARD_DISPLAY_TYPE);
		if (type == null) {
			return TaskAttribute.CardDisplayType.NONE;
		} else {
			return type;
		}
	}


}
