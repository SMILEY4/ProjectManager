package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class AttributeRenamedRejection extends Event {


	private TaskAttribute attribute;
	private String newName;
	private com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause;



	public AttributeRenamedRejection(TaskAttribute attribute, String newName, com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.newName = newName;
		this.cause = cause;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public String getNewName() {
		return newName;
	}




	public EventCause getCause() {
		return cause;
	}

}
