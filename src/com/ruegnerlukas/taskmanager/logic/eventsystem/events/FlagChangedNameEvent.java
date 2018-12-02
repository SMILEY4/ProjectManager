package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagChangedNameEvent extends Event {

	
	private TaskFlag flag;
	private String oldName, newName;
	
	
	public FlagChangedNameEvent(TaskFlag flag, String oldName, String newName, Object source) {
		super(source);
		this.flag = flag;
		this.oldName = oldName;
		this.newName = newName;
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

}
