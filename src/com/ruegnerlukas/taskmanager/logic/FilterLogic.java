package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.filter.AttributeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	protected FilterLogic() {

		// when an attribute was deleted
		EventManager.registerListener(e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			onAttributeDeleted(event.getAttribute());
		}, AttributeRemovedEvent.class);

	}




	private void onAttributeDeleted(TaskAttribute attribute) {
		removeFilterCriteria(attribute);
	}




	protected boolean isAttributeRelevant(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		for (FilterCriteria criteria : project.filterCriteria) {
			if (criteria.attribute == attribute) {
				return true;
			}
		}
		return false;
	}




	protected List<Task> applyFilters(List<Task> tasksInput) {

		Project project = Logic.project.getProject();

		List<FilterCriteria> filters = project.filterCriteria;
		List<Task> filtered = new ArrayList<>();

		for (int i = 0, n = tasksInput.size(); i < n; i++) {
			Task task = tasksInput.get(i);
			boolean match = match(task, filters);
			if (match) {
				filtered.add(task);
			}
		}

		return filtered;
	}




	private boolean match(Task task, List<FilterCriteria> filters) {
		for (FilterCriteria filter : filters) {
			AttributeFilter attributeFilter = AttributeLogic.FILTER_MAP.get(filter.attribute.data.getType());
			boolean isMatch = attributeFilter.match(task, filter);
			if (isMatch) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}




	protected List<FilterCriteria> getFilterCriteria(TaskAttribute attribute) {
		List<FilterCriteria> list = new ArrayList<>();
		for (FilterCriteria criteria : Logic.project.getProject().filterCriteria) {
			if (criteria.attribute == attribute) {
				list.add(criteria);
			}
		}
		return list;
	}


	//======================//
	//        GETTER        //
	//======================//




	public void getFilterCriteria(Request<List<FilterCriteria>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.filterCriteria));
		}
	}




	public void getSavedFilterCriterias(Request<Map<String, List<FilterCriteria>>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.savedFilters));
		}
	}




	public void getSavedFilterCriteria(String name, Request<List<FilterCriteria>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedFilters.containsKey(name)) {
				request.respond(new Response<>(Response.State.SUCCESS, project.savedFilters.get(name)));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "No presets filters with name '" + name + "'"));
			}
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	public void saveFilterCriterias(String name, List<FilterCriteria> filterCriterias) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedFilters.containsKey(name)) {
				EventManager.fireEvent(new FilterCriteriaSavedRejection(name, filterCriterias, EventCause.NOT_UNIQUE, this));
			} else {
				project.savedFilters.put(name, filterCriterias);
				EventManager.fireEvent(new FilterCriteriaSavedEvent(name, filterCriterias, this));
			}
		}
	}




	public void deleteSavedFilterCriteria(String name) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.savedFilters.containsKey(name)) {
				List<FilterCriteria> list = project.savedFilters.get(name);
				project.savedFilters.remove(name);
				EventManager.fireEvent(new FilterCriteriaDeletedSavedEvent(name, list, this));
			} else {
				EventManager.fireEvent(new FilterCriteriaDeleteSavedRejection(name, EventCause.NOT_EXISTS, this));
			}
		}
	}




	/**
	 * Replaces the filterCriteria-list with the new given list<p>
	 * Events:<p>
	 * - FilterCriteriaChangedEvent: when the list of criteria changed
	 */
	public void setFilterCriteria(List<FilterCriteria> filterCriteria) {
		Project project = Logic.project.getProject();
		if (project != null) {
			project.filterCriteria.clear();
			project.filterCriteria.addAll(filterCriteria);
			EventManager.fireEvent(new FilterCriteriaChangedEvent(project.filterCriteria, this));
		}
	}




	/**
	 * Removes all filter-criteria associated with the given attribute<p>
	 * Events:<p>
	 * - FilterCriteriaChangedEvent: when the list of criteria changed
	 */
	public void removeFilterCriteria(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<FilterCriteria> toRemove = new ArrayList<>();
			for (FilterCriteria element : project.filterCriteria) {
				if (element.attribute == attribute) {
					toRemove.add(element);
				}
			}
			project.filterCriteria.removeAll(toRemove);
			if (!toRemove.isEmpty()) {
				EventManager.fireEvent(new FilterCriteriaChangedEvent(project.filterCriteria, this));
			}
		}
	}


}
