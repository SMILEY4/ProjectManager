package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

public class AttributeCreatedRejection extends Event {


	private TaskAttribute attribute;
	private EventCause cause;




	public AttributeCreatedRejection(TaskAttribute attribute, EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.cause = cause;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public EventCause getCause() {
		return cause;
	}

}
