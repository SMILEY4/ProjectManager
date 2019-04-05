package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupOrderDeletedRejection extends Event {


	private String name;
	private List<TaskAttribute> elements;
	private com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause;




	public GroupOrderDeletedRejection(String name, com.ruegnerlukas.taskmanager.architecture.eventsystem.events.EventCause cause, Object source) {
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
