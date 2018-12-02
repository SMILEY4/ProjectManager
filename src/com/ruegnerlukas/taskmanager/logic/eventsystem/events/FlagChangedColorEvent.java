package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class FlagChangedColorEvent extends Event {

	private TaskFlag flag;
	private FlagColor oldColor, newColor;
	
	
	public FlagChangedColorEvent(TaskFlag flag, FlagColor oldColor, FlagColor newColor, Object source) {
		super(source);
		this.flag = flag;
		this.oldColor = oldColor;
		this.newColor = newColor;
	}
	
	
	public TaskFlag getFlag() {
		return flag;
	}
	
	
	public FlagColor getOldColor() {
		return oldColor;
	}

	
	public FlagColor getNewColor() {
		return newColor;
	}
	
}
