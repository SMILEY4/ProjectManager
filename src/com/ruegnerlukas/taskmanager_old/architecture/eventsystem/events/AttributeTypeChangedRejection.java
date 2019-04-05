package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;

public class AttributeTypeChangedRejection extends Event {


	private TaskAttribute attribute;
	private TaskAttributeType newType;
	private com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause;



	public AttributeTypeChangedRejection(TaskAttribute attribute, TaskAttributeType newType, com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.newType = newType;
		this.cause = cause;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeType getNewType() {
		return newType;
	}




	public EventCause getCause() {
		return cause;
	}

}
