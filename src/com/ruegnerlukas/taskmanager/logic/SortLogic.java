package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
		Comparator<TaskGroup> compGroups = (a, b) -> {
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
		taskGroupData.groups.sort(compGroups);

		Comparator<Task> compTasks = (a, b) -> {
			for (SortElement element : Logic.project.getProject().sortElements) {
				TaskAttribute attrib = element.attribute;
				TaskAttributeValue valueA = Logic.tasks.getValue(a, attrib);
				TaskAttributeValue valueB = Logic.tasks.getValue(b, attrib);
				int cmp = valueA.compareTo(valueB) * (element.sortDir == SortElement.Sort.DESC ? -1 : +1);
				if (cmp != 0) {
					return cmp;
				}
			}
			return 0;
		};
		for (TaskGroup group : taskGroupData.groups) {
			group.tasks.sort(compTasks);
		}
	}


	//======================//
	//        GETTER        //
	//======================//




	public void getSavedSortElements(Request<Map<String, List<SortElement>>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.savedSortElements));
		}
	}




	public void getSavedSortElements(String name, Request<List<SortElement>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedSortElements.containsKey(name)) {
				request.respond(new Response<>(Response.State.SUCCESS, project.savedSortElements.get(name)));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "No presets elements with name '" + name + "' found"));
			}
		}
	}




	public void getSortElements(Request<List<SortElement>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.sortElements));
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	public void saveSortElements(String name, List<SortElement> elements) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedSortElements.containsKey(name)) {
				EventManager.fireEvent(new SortSavedRejection(name, elements, EventCause.NOT_UNIQUE, this));
			} else {
				project.savedSortElements.put(name, elements);
				EventManager.fireEvent(new SortSavedEvent(name, elements, this));
			}
		}
	}




	public void deleteSavedSortElements(String name) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedSortElements.containsKey(name)) {
				List<SortElement> elements = project.savedSortElements.get(name);
				project.savedSortElements.remove(name);
				EventManager.fireEvent(new SortDeleteSavedEvent(name, elements, this));
			} else {
				EventManager.fireEvent(new SortDeleteSavedRejection(name, EventCause.NOT_EXISTS, this));
			}
		}
	}




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
