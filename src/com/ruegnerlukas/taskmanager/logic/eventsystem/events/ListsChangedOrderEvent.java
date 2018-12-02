package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import java.util.List;

import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ListsChangedOrderEvent extends Event {

	private List<Integer> oldOrder, newOrder;
	
	
	public ListsChangedOrderEvent(List<Integer> oldOrder, List<Integer> newOrder, Object source) {
		super(source);
		this.oldOrder = oldOrder;
		this.newOrder = newOrder;
	}
	
	
	public List<Integer> getOldOrder() {
		return oldOrder;
	}

	
	public List<Integer> getNewOrder() {
		return newOrder;
	}
	
	

}
