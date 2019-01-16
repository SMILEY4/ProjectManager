package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.SortElementsChangedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class SortLogic {

	public boolean setSort(List<SortElement> elements) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			project.sortElements.clear();
			project.sortElements.addAll(elements);
			EventManager.fireEvent(new SortElementsChangedEvent(project.sortElements, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean removeSortElement(TaskAttribute attribute) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			List<SortElement> toRemove = new ArrayList<>();
			for(SortElement element : project.sortElements) {
				if(element.attribute == attribute) {
					toRemove.add(element);
				}
			}
			project.sortElements.removeAll(toRemove);
			if(!toRemove.isEmpty()) {
				EventManager.fireEvent(new SortElementsChangedEvent(project.sortElements, this));
			}
			return !toRemove.isEmpty();
		} else {
			return false;
		}
	}


}
