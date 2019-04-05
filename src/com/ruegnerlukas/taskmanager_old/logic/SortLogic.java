package com.ruegnerlukas.taskmanager_old.logic;

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
import com.ruegnerlukas.taskmanager.logic.Logic;

import java.util.*;

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
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
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
			for (SortElement element : com.ruegnerlukas.taskmanager.logic.Logic.project.getProject().sortElements) {
				TaskAttribute attrib = element.attribute;
				TaskAttributeValue valueA = com.ruegnerlukas.taskmanager.logic.Logic.tasks.getValue(a, attrib);
				TaskAttributeValue valueB = com.ruegnerlukas.taskmanager.logic.Logic.tasks.getValue(b, attrib);
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




	public Response<Map<String, List<SortElement>>> getSavedSortElements() {
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
		if (project != null) {
			return new Response<Map<String, List<SortElement>>>().complete(project.savedSortElements);
		} else {
			return new Response<Map<String, List<SortElement>>>().complete(new HashMap<>(), Response.State.FAIL);
		}
	}




	public Response<List<SortElement>> getSavedSortElements(String name) {
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
		if (project != null) {
			if (project.savedSortElements.containsKey(name)) {
				return new Response<List<SortElement>>().complete(project.savedSortElements.get(name));
			} else {
				return new Response<List<SortElement>>().complete(new ArrayList<>(), Response.State.FAIL);
			}
		} else {
			return new Response<List<SortElement>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	public Response<List<SortElement>> getSortElements() {
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
		if (project != null) {
			return new Response<List<SortElement>>().complete(project.sortElements);
		} else {
			return new Response<List<SortElement>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	public void saveSortElements(String name, List<SortElement> elements) {
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
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
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
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
		Project project = com.ruegnerlukas.taskmanager.logic.Logic.project.getProject();
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
