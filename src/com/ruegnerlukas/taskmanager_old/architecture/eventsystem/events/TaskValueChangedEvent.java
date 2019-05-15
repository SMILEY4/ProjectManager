package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class TaskValueChangedEvent extends Event {


	private Task task;
	private TaskAttribute attribute;
	private TaskAttributeValue newValue;
	private TaskAttributeValue oldValue;



	public TaskValueChangedEvent(Task task, TaskAttribute attribute, TaskAttributeValue oldValue, TaskAttributeValue newValue, Object source) {
		super(source);
		this.task = task;
		this.attribute = attribute;
		this.newValue = newValue;
		this.oldValue = oldValue;
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



}
