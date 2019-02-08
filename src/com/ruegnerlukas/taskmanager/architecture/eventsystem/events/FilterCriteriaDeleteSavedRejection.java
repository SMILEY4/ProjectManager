package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public class FilterCriteriaDeleteSavedRejection extends Event {


	private String name;
	private EventCause cause;




	public FilterCriteriaDeleteSavedRejection(String name, EventCause cause, Object source) {
		super(source);
		this.name = name;
		this.cause = cause;
	}




	public String getName() {
		return name;
	}




	public EventCause getCause() {
		return cause;
	}

}
