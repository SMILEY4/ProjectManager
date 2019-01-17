package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.Task;

public class TaskCreatedEvent extends Event {


	private Task task;




	public TaskCreatedEvent(Task task, Object source) {
		super(source);
		this.task = task;
	}




	public Task getTask() {
		return this.task;
	}


}
