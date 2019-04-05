package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;

import java.util.List;

public class SortDeleteSavedEvent extends Event {


	private String name;
	private List<SortElement> elements;




	public SortDeleteSavedEvent(String name, List<SortElement> elements, Object source) {
		super(source);
		this.name = name;
		this.elements = elements;
	}




	public String getName() {
		return name;
	}




	public List<SortElement> getElements() {
		return this.elements;
	}


}
