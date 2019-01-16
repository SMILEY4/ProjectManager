package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.sorting.SortElement;

import java.util.List;

public class SortElementsChangedEvent extends Event {


	private List<SortElement> sortElements;




	public SortElementsChangedEvent(List<SortElement> sortElements, Object source) {
		super(source);
		this.sortElements = sortElements;
	}




	public List<SortElement> getSortElements() {
		return this.sortElements;
	}


}
