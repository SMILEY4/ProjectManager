package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

import javafx.scene.paint.Color;

public class ListChangedColorEvent extends Event {

	private TaskList list;
	private Color oldColor, newColor;
	
	
	public ListChangedColorEvent(TaskList list, Color oldColor, Color newColor, Object source) {
		super(source);
		this.list = list;
		this.oldColor = oldColor;
		this.newColor = newColor;
	}
	
	
	public Color getOldColor() {
		return oldColor;
	}

	
	public Color getNewColor() {
		return newColor;
	}
	
	
	public TaskList getList() {
		return list;
	}

}
