package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.FilterCriteriaChangedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;

import java.util.List;

public class FilterLogic {

	public boolean setFilterCriteria(List<FilterCriteria> filterCriteria) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			project.filterCriteria.clear();
			project.filterCriteria.addAll(filterCriteria);
			EventManager.fireEvent(new FilterCriteriaChangedEvent(project.filterCriteria, this));
			return true;
		} else {
			return false;
		}
	}



}
