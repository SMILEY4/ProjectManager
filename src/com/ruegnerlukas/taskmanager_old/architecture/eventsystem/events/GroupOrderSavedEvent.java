package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupOrderSavedEvent extends Event {


	private String name;
	private List<TaskAttribute> elements;




	public GroupOrderSavedEvent(String name, List<TaskAttribute> elements, Object source) {
		super(source);
		this.name = name;
		this.elements = elements;
	}




	public String getName() {
		return name;
	}




	public List<TaskAttribute> getAttributes() {
		return this.elements;
	}


}
