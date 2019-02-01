package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Task;

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
