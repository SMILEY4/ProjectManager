package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;

import java.util.List;

public class FilterCriteriaChangedEvent extends Event {


	private List<FilterCriteria> filterCriteria;




	public FilterCriteriaChangedEvent(List<FilterCriteria> filterCriteria, Object source) {
		super(source);
		this.filterCriteria = filterCriteria;
	}




	public List<FilterCriteria> getAttributeOrder() {
		return this.filterCriteria;
	}


}
