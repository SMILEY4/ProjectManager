package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class TaskChangedTextEvent extends Event {

	private TaskCard card;
	private String oldText, newText;
	
	
	public TaskChangedTextEvent(TaskCard task, String oldText, String newText, Object source) {
		super(source);
		this.card = task;
		this.oldText = oldText;
		this.newText = newText;
	}
	
	
	
	
	public TaskCard getTask() {
		return card;
	}
	
	
	
	
	public String getOldText() {
		return oldText;
	}
	
	
	
	
	public String getNewText() {
		return newText;
	}
	
	
	
}
