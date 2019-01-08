package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeCreatedRejection extends Event {

	private CustomAttribute attrib;
	private EventCause cause;
	
	
	public CustomAttributeCreatedRejection(CustomAttribute attrib, EventCause cause, Object source) {
		super(source);
		this.attrib = attrib;
		this.cause = cause;
	}
	
	
	public CustomAttribute getRejectedAttribute() {
		return attrib;
	}

	
	public EventCause getCause() {
		return this.cause;
	}
	
	
}
