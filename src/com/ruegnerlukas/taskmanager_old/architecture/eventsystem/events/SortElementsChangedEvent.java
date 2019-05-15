package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;

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
