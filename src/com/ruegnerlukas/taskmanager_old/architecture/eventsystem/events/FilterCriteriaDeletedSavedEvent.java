package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;

import java.util.List;

public class FilterCriteriaDeletedSavedEvent extends Event {


	private String name;
	private List<FilterCriteria> filterCriteria;




	public FilterCriteriaDeletedSavedEvent(String name, List<FilterCriteria> filterCriteria, Object source) {
		super(source);
		this.name = name;
		this.filterCriteria = filterCriteria;
	}




	public String getName() {
		return name;
	}




	public List<FilterCriteria> getFilterCriteria() {
		return this.filterCriteria;
	}


}
