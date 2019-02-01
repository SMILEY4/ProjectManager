package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class AttributeRemovedRejection extends Event {


	private TaskAttribute attribute;
	private EventCause cause;



	public AttributeRemovedRejection(TaskAttribute attribute, EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.cause = cause;
	}




	public EventCause getCause() {
		return cause;
	}




	public TaskAttribute getAttribute() {
		return this.attribute;
	}


}
