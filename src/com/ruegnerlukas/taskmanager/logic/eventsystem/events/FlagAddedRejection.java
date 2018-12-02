package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagAddedRejection extends Event {

	private String name;
	private EventCause cause;
	
	public FlagAddedRejection(String name, EventCause cause, Object source) {
		super(source);
		this.name = name;
		this.cause = cause;
	}
	
	
	public String getRejectedFlagName() {
		return name;
	}
	
	
	public EventCause getCause() {
		return cause;
	}

}
