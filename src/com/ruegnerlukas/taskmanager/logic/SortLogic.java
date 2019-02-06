package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.SortElementsChangedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	protected SortLogic() {
		EventManager.registerListener(e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			onAttributeDeleted(event.getAttribute());
		}, AttributeRemovedEvent.class);
	}




	private void onAttributeDeleted(TaskAttribute attribute) {
		removeSortElement(attribute);
	}




	protected boolean isAttributeRelevant(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		for (SortElement sortElement : project.sortElements) {
			if (sortElement.attribute == attribute) {
				return true;
			}
		}
		return false;
	}




	protected void applySort(TaskGroupData taskGroupData) {
		Comparator<TaskGroup> comp = (a, b) -> {
			for (int i = 0; i < taskGroupData.attributes.size(); i++) {
				TaskAttribute attrib = taskGroupData.attributes.get(i);
				TaskAttributeValue valueA = a.values.get(attrib);
				TaskAttributeValue valueB = b.values.get(attrib);
				int cmp = valueA.compareTo(valueB);
				if (cmp != 0) {
					return cmp;
				}
			}
			return 0;
		};
	}


	//======================//
	//        GETTER        //
	//======================//




	public void getSortElements(Request<List<SortElement>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.sortElements));
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	/**
	 * Replaces the list of SortElements with the new given list<p>
	 * Events:<p>
	 * - SortElementsChangedEvent: when the list of SortElements has changed
	 */
	public void setSort(List<SortElement> elements) {
		Project project = Logic.project.getProject();
		if (project != null) {
			project.sortElements.clear();
			project.sortElements.addAll(elements);
			EventManager.fireEvent(new SortElementsChangedEvent(project.sortElements, this));
		}
	}




	/**
	 * Removes a SortElement associated with the given attribute<p>
	 * Events:<p>
	 * - SortElementsChangedEvent: when the list of SortElements has changed
	 */
	public void removeSortElement(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<SortElement> toRemove = new ArrayList<>();
			for (SortElement element : project.sortElements) {
				if (element.attribute == attribute) {
					toRemove.add(element);
				}
			}
			project.sortElements.removeAll(toRemove);
			if (!toRemove.isEmpty()) {
				EventManager.fireEvent(new SortElementsChangedEvent(project.sortElements, this));
			}
		}
	}


}
