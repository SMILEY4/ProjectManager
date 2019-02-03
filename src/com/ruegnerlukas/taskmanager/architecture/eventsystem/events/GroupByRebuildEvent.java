package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;

public class GroupByRebuildEvent extends Event {

	private TaskGroupData data;


	public GroupByRebuildEvent(TaskGroupData data, Object source) {
		super(source);
		this.data = data;
	}

	
	
	
	public TaskGroupData getGroupByData() {
		return data;
	}

	
}
