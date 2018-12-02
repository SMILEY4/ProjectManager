package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagChangedNameRejection extends Event {

	private TaskFlag flag;
	private String oldName, newName;
	private EventCause cause;
	
	public FlagChangedNameRejection(TaskFlag flag, String oldName, String newName, EventCause cause, Object source) {
		super(source);
		this.flag = flag;
		this.oldName = oldName;
		this.newName = newName;
		this.cause = cause;
	}
	
	
	public TaskFlag getFlag() {
		return flag;
	}
	
	
	public String getOldName() {
		return oldName;
	}

	
	public String getNewName() {
		return newName;
	}
	
	
	public EventCause getCause() {
		return this.cause;
	}

}
