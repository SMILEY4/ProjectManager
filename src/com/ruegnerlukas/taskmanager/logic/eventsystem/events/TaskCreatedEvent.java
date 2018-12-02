package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class TaskCreatedEvent extends Event {

	private TaskList list;
	private TaskCard card;
	
	
	public TaskCreatedEvent(TaskList list, TaskCard task, Object source) {
		super(source);
		this.list = list;
		this.card = task;
	}
	
	
	public TaskCard getCard() {
		return card;
	}
	
	public TaskList getList() {
		return list;
	}

	
	
}
