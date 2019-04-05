package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public class GroupHeaderChangedEvent extends Event {


	private String oldString, newString;




	public GroupHeaderChangedEvent(String oldString, String newString, Object source) {
		super(source);
		this.oldString = oldString;
		this.newString = newString;
	}




	public String getOldString() {
		return oldString;
	}




	public String getNewString() {
		return newString;
	}


}
