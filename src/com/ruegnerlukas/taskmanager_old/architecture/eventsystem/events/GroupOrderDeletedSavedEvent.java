package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;

public class GroupOrderDeletedSavedEvent extends Event {


	private String name;
	private AttributeGroupData data;




	public GroupOrderDeletedSavedEvent(String name, AttributeGroupData data, Object source) {
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
