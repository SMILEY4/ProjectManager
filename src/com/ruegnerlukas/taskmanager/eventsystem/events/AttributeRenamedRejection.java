package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;

public class AttributeRenamedRejection extends Event {


	private TaskAttribute attribute;
	private String newName;
	private EventCause cause;



	public AttributeRenamedRejection(TaskAttribute attribute, String newName, EventCause cause, Object source) {
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
