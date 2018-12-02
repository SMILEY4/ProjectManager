package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ListChangedNameEvent extends Event {

	private TaskList list;
	private String oldName, newName;
	
	
	public ListChangedNameEvent(TaskList list, String oldName, String newName, Object source) {
		super(source);
		this.list = list;
		this.oldName = oldName;
		this.newName = newName;
	}
	
	
	public String getOldName() {
		return oldName;
	}

	
	public String getNewName() {
		return newName;
	}
	
	
	public TaskList getList() {
		return list;
	}

}
