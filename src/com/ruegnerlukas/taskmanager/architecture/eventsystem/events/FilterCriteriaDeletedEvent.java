package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;

import java.util.List;

public class FilterCriteriaDeletedEvent extends Event {


	private String name;
	private List<FilterCriteria> filterCriteria;




	public FilterCriteriaDeletedEvent(String name, List<FilterCriteria> filterCriteria, Object source) {
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
