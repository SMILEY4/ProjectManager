package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

import java.util.List;

public class GroupByOrderChangedEvent extends Event {


	private List<TaskAttribute> order;




	public GroupByOrderChangedEvent(List<TaskAttribute> order, Object source) {
		super(source);
		this.order = order;
	}




	public List<TaskAttribute> getAttributeOrder() {
		return this.order;
	}


}
