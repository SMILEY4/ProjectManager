package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import java.util.List;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;

public class ListMovedEvent extends ListsChangedOrderEvent {

	
	private TaskList list;
	
	
	public ListMovedEvent(TaskList list, List<Integer> oldOrder, List<Integer> newOrder, Object source) {
		super(oldOrder, newOrder, source);
		this.list = list;
	}
	
	
	public TaskList getList() {
		return list;
	}
	
	
	

}
