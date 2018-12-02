package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagAddedEvent extends Event {

	private TaskFlag flag;
	
	public FlagAddedEvent(TaskFlag flag, Object source) {
		super(source);
		this.flag = flag;
	}
	
	
	public TaskFlag getAddedFlag() {
		return flag;
	}

}
