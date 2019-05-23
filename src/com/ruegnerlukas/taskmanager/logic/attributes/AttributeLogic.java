package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

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




	public static boolean setAttributeValue(Project project, TaskAttribute attribute, AttributeValue<?> value, boolean preferNoValueTask) {

		// validate value
		// TODO ?

		// set value
		AttributeValue<?> prevValue = attribute.values.get(value.getType());
		attribute.values.put(value.getType(), value);
		onAttributeValueChanged(attribute, value.getType(), prevValue, value);

		// check tasks for changes
		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskValue<?> taskValue = TaskLogic.getTaskValue(task, attribute);
			if (!AttributeLogicManager.isValidTaskValue(attribute, taskValue)) {
				TaskValue<?> validValue = AttributeLogicManager.generateValidTaskValue(taskValue, attribute, preferNoValueTask);
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




	private static void onAttributeValueChanged(TaskAttribute attribute, AttributeValueType type, AttributeValue<?> prevValue, AttributeValue<?> newValue) {
		AttributeValueChangeEvent event = new AttributeValueChangeEvent(attribute, type, prevValue, newValue);
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
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	public static TaskValue<?> getDefaultValue(TaskAttribute attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return value.getValue();
		}
	}




	public static void setShowOnTaskCard(TaskAttribute attribute, boolean showOnCard) {
		CardDisplayTypeValue prev = (CardDisplayTypeValue) attribute.values.get(AttributeValueType.CARD_DISPLAY_TYPE);
		CardDisplayTypeValue value = new CardDisplayTypeValue(showOnCard);
		attribute.values.put(AttributeValueType.CARD_DISPLAY_TYPE, value);
		onAttributeValueChanged(attribute, AttributeValueType.CARD_DISPLAY_TYPE, prev, value);
	}




	public static boolean getShowOnTaskCard(TaskAttribute attribute) {
		CardDisplayTypeValue value = (CardDisplayTypeValue) attribute.values.get(AttributeValueType.CARD_DISPLAY_TYPE);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}


}
