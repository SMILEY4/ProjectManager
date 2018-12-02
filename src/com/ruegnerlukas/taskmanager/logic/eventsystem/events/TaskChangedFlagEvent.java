package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class TaskChangedFlagEvent extends Event {

	private TaskCard card;
	private TaskFlag oldFlag, newFlag;
	
	
	public TaskChangedFlagEvent(TaskCard task, TaskFlag oldFlag, TaskFlag newFlag, Object source) {
		super(source);
		this.card = task;
		this.oldFlag = oldFlag;
		this.newFlag = newFlag;
	}
	
	
	
	
	public TaskCard getTask() {
		return card;
	}
	
	
	
	
	public TaskFlag getOldFlag() {
		return oldFlag;
	}
	
	
	
	
	public TaskFlag getNewFlag() {
		return newFlag;
	}
	
	
	
}
