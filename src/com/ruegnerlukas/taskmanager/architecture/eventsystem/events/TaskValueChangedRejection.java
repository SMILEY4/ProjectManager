package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class TaskValueChangedRejection extends Event {


	private Task task;
	private TaskAttribute attribute;
	private TaskAttributeValue newValue;
	private TaskAttributeValue oldValue;
	private EventCause cause;




	public TaskValueChangedRejection(Task task, TaskAttribute attribute, TaskAttributeValue oldValue, TaskAttributeValue newValue,
									 EventCause cause, Object source) {
		super(source);
		this.task = task;
		this.attribute = attribute;
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.cause = cause;
	}




	public Task getTask() {
		return this.task;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeValue getNewValue() {
		return newValue;
	}




	public TaskAttributeValue getOldValue() {
		return oldValue;
	}




	public boolean wasCleared() {
		return newValue == null;
	}




	public EventCause getCause() {
		return cause;
	}

}
