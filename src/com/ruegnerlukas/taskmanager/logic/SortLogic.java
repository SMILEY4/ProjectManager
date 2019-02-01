package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.SortElementsChangedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class SortLogic {




	protected SortLogic() {
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent)e;
				removeSortElement(event.getAttribute());
			}
		}, AttributeRemovedEvent.class);
	}




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
