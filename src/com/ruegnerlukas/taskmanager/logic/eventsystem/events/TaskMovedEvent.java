package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class TaskMovedEvent extends Event {

	private TaskCard card;
	private TaskList oldList, newList;
	
	
	public TaskMovedEvent(TaskCard task, TaskList oldList, TaskList newList, Object source) {
		super(source);
		this.card = task;
		this.oldList = oldList;
		this.newList = newList;
	}
	
	
	
	
	public TaskCard getTask() {
		return card;
	}
	
	
	
	
	public TaskList getOldList() {
		return oldList;
	}
	
	
	
	
	public TaskList getNewList() {
		return newList;
	}
	
	
	
}
