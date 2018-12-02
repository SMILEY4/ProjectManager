package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ListChangedVisibilityEvent extends Event {

	private TaskList list;
	private boolean wasHidden, isHidden;
	
	
	public ListChangedVisibilityEvent(TaskList list, boolean wasHidden, boolean isHidden, Object source) {
		super(source);
		this.list = list;
		this.wasHidden = wasHidden;
		this.isHidden = isHidden;
	}
	
	
	public boolean wasListHidden() {
		return wasHidden;
	}

	
	public boolean isListHidden() {
		return isHidden;
	}
	
	
	public TaskList getList() {
		return list;
	}

}
