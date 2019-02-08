package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupOrderDeletedRejection extends Event {


	private String name;
	private List<TaskAttribute> elements;
	private EventCause cause;




	public GroupOrderDeletedRejection(String name, EventCause cause, Object source) {
		super(source);
		this.name = name;
		this.elements = elements;
		this.cause = cause;
	}




	public String getName() {
		return name;
	}




	public List<TaskAttribute> getAttributes() {
		return this.elements;
	}




	public EventCause getCause() {
		return cause;
	}

}
