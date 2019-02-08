package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;

public class GroupOrderDeletedEvent extends Event {


	private String name;
	private AttributeGroupData data;




	public GroupOrderDeletedEvent(String name, AttributeGroupData data, Object source) {
		super(source);
		this.name = name;
		this.data = data;
	}




	public String getName() {
		return name;
	}




	public AttributeGroupData getData() {
		return this.data;
	}


}
