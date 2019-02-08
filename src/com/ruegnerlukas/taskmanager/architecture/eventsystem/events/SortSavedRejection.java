package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;

import java.util.List;

public class SortSavedRejection extends Event {


	private String name;
	private List<SortElement> elements;
	private EventCause cause;




	public SortSavedRejection(String name, List<SortElement> elements, EventCause cause, Object source) {
		super(source);
		this.name = name;
		this.elements = elements;
		this.cause = cause;
	}




	public String getName() {
		return name;
	}




	public List<SortElement> getElements() {
		return this.elements;
	}




	public EventCause getCause() {
		return cause;
	}

}
