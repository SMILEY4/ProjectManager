package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.RequestEvent;
import com.ruegnerlukas.taskmanager.logic.services.ListService.ListDeleteBehavior;

public class ListDeleteRequest extends RequestEvent<ListDeleteBehavior> {

	
	private TaskList list;
	
	
	public ListDeleteRequest(TaskList list, Object source) {
		super(source);
		this.list = list;
	}
	
	
	public TaskList getList() {
		return list;
	}

}
