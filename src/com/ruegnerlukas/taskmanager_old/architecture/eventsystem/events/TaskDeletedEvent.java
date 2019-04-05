package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.Task;

public class TaskDeletedEvent extends Event {


	private Task task;




	public TaskDeletedEvent(Task task, Object source) {
		super(source);
		this.task = task;
	}




	public Task getTask() {
		return this.task;
	}


}
