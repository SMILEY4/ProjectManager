package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;

import java.util.List;

public class FilterCriteriaSavedRejection extends Event {


	private String name;
	private List<FilterCriteria> filterCriteria;
	private EventCause cause;




	public FilterCriteriaSavedRejection(String name, List<FilterCriteria> filterCriteria, EventCause cause, Object source) {
		super(source);
		this.name = name;
		this.filterCriteria = filterCriteria;
		this.cause = cause;
	}




	public String getName() {
		return name;
	}




	public List<FilterCriteria> getFilterCriteria() {
		return this.filterCriteria;
	}




	public EventCause getCause() {
		return cause;
	}

}
