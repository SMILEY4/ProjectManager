package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagRemovedEvent extends Event {

	private TaskFlag flag;
	
	public FlagRemovedEvent(TaskFlag flag, Object source) {
		super(source);
		this.flag = flag;
	}
	
	
	public TaskFlag getRemovedFlag() {
		return flag;
	}
	
}
