package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.groups.GroupByData;

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
