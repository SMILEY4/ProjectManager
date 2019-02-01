package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public class ProjectRenamedEvent extends Event {

	private String oldName, newName;
	
	
	public ProjectRenamedEvent(String oldName, String newName, Object source) {
		super(source);
		this.oldName = oldName;
		this.newName = newName;
	}

	
	
	
	public String getOldName() {
		return oldName;
	}
	
	
	
	
	public String getNewName() {
		return newName;
	}
	
	
	
}
