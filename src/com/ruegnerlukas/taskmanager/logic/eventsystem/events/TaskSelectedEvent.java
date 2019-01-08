package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class TaskSelectedEvent extends Event {

	private TaskCard task;
	
	
	public TaskSelectedEvent(TaskCard task, Object source) {
		super(source);
		this.task = task;
	}
	
	
	
	
	public TaskCard getTask() {
		return task;
	}
	
	
	
}
