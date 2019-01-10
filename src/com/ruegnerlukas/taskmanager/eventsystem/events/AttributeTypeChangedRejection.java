package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;

public class AttributeTypeChangedRejection extends Event {


	private TaskAttribute attribute;
	private TaskAttributeType newType;
	private EventCause cause;



	public AttributeTypeChangedRejection(TaskAttribute attribute, TaskAttributeType newType, EventCause cause, Object source) {
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
