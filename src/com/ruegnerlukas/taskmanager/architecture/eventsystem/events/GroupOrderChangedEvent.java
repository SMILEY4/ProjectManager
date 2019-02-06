package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupOrderChangedEvent extends Event {


	private List<TaskAttribute> elements;




	public GroupOrderChangedEvent(List<TaskAttribute> elements, Object source) {
		super(source);
		this.elements = elements;
	}




	public List<TaskAttribute> getAttributes() {
		return this.elements;
	}


}