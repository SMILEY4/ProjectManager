package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.groups.GroupByData;

public class GroupByRebuildEvent extends Event {

	private GroupByData data;


	public GroupByRebuildEvent(GroupByData data, Object source) {
		super(source);
		this.data = data;
	}

	
	
	
	public GroupByData getGroupByData() {
		return data;
	}

	
}
