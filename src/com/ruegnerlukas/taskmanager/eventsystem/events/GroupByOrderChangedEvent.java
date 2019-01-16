package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupByOrderChangedEvent extends Event {


	private List<TaskAttribute> elements;




	public GroupByOrderChangedEvent(List<TaskAttribute> elements, Object source) {
		super(source);
		this.elements = elements;
	}




	public List<TaskAttribute> getAttributes() {
		return this.elements;
	}


}
