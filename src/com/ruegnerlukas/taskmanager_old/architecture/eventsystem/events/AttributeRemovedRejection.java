package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class AttributeRemovedRejection extends Event {


	private TaskAttribute attribute;
	private com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause;



	public AttributeRemovedRejection(TaskAttribute attribute, com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause, Object source) {
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
