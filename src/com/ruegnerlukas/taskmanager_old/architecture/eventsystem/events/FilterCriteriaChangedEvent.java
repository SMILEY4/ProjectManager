package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;

import java.util.List;

public class FilterCriteriaChangedEvent extends Event {


	private List<FilterCriteria> filterCriteria;




	public FilterCriteriaChangedEvent(List<FilterCriteria> filterCriteria, Object source) {
		super(source);
		this.filterCriteria = filterCriteria;
	}




	public List<FilterCriteria> getFilterCriteria() {
		return this.filterCriteria;
	}


}
