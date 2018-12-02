package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ListDeletedEvent extends Event {

	private TaskList list;
	
	public ListDeletedEvent(TaskList list, Object source) {
		super(source);
		this.list = list;
	}
	
	
	public TaskList getDeletedList() {
		return list;
	}
	
}
