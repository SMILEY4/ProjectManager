package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;

public class GroupByHeaderChangedEvent extends Event {


	private String oldString, newString;




	public GroupByHeaderChangedEvent(String oldString, String newString, Object source) {
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
