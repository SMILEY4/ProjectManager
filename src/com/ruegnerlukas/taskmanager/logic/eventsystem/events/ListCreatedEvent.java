package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ListCreatedEvent extends Event {

	private TaskList list;
	private int index;
	
	public ListCreatedEvent(TaskList list, int index, Object source) {
		super(source);
		this.list = list;
		this.index = index;
	}
	
	
	public TaskList getCreatedList() {
		return list;
	}

	
	public int getIndex() {
		return index;
	}
	
}
