package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;

import java.util.ArrayList;
import java.util.List;

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

		// when anything else changed
		EventManager.registerListener(this, e -> {
			onChange();
		}, FilterCriteriaChangedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class);

	}




	private void onAttributeDeleted(TaskAttribute attribute) {
		removeFilterCriteria(attribute);
	}




	private void onChange() {
		applyFilters();
	}




	private boolean match(Task task, List<FilterCriteria> filters) {

		for (FilterCriteria filter : filters) {

			final TaskAttribute attribute = filter.attribute;
			final TaskAttributeType attributeType = attribute.data.getType();
			final FilterCriteria.ComparisonOp comp = filter.comparisonOp;

			final TaskAttributeValue valueComp = filter.comparisionValue;
			final TaskAttributeValue valueTask = Logic.tasks.getValue(task, attribute);

			if (valueTask instanceof NoValue) {
				return false;
			}

			if (valueTask.getClass() != valueComp.getClass()) {
				return false;
			}


			if (comp == FilterCriteria.ComparisonOp.EQUALITY) {
				if (valueTask.compareTo(valueComp) == 0) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.INEQUALITY) {
				if (valueTask.compareTo(valueComp) != 0) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.LESS_THAN) {
				if (valueTask.compareTo(valueComp) == -1) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.LESS_THAN_EQUAL) {
				if (valueTask.compareTo(valueComp) != +1) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.GREATER_THAN) {
				if (valueTask.compareTo(valueComp) == +1) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.GREATER_THAN_EQUAL) {
				if (valueTask.compareTo(valueComp) != -1) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.IN_RANGE) {
				NumberValue value = (NumberValue) valueTask;
				NumberPairValue range = (NumberPairValue) valueComp;
				if (range.inRange(value)) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.NOT_IN_RANGE) {
				NumberValue value = (NumberValue) valueTask;
				NumberPairValue range = (NumberPairValue) valueComp;
				if (!range.inRange(value)) {
					continue;
				} else {
					return false;
				}
			}

			if (comp == FilterCriteria.ComparisonOp.IN_LIST) {
				if (attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.TEXT) {
					if (inTextArray((TextArrayValue) valueComp, (TextValue) valueTask)) {
						continue;
					} else {
						return false;
					}
				}
				if (attributeType == TaskAttributeType.ID || attributeType == TaskAttributeType.NUMBER) {
					TextArrayValue list = (TextArrayValue) valueComp;
					NumberValue numberValue = (NumberValue) valueTask;
					if (inTextArray(list, numberValue)) {
						continue;
					} else {
						return false;
					}
				}
				if (attributeType == TaskAttributeType.FLAG) {
					FlagArrayValue list = (FlagArrayValue) valueComp;
					FlagValue flagValue = (FlagValue) valueTask;
					if (inFlagArray(list, flagValue)) {
						continue;
					} else {
						return false;
					}
				}
			}

			if (comp == FilterCriteria.ComparisonOp.NOT_IN_LIST) {
				if (attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.TEXT) {
					if (inTextArray((TextArrayValue) valueComp, (TextValue) valueTask)) {
						return false;
					} else {
						continue;
					}
				}
				if (attributeType == TaskAttributeType.ID || attributeType == TaskAttributeType.NUMBER) {
					TextArrayValue list = (TextArrayValue) valueComp;
					NumberValue numberValue = (NumberValue) valueTask;
					boolean inList = false;
					for (String listElement : list.getText()) {
						if (numberValue.isInt()) {
							int element = Integer.parseInt(listElement);
							if (element == numberValue.getInt()) {
								inList = true;
								break;
							}
						} else {
							double element = Double.parseDouble(listElement);
							int dec = listElement.contains(".") ? listElement.split("\\.")[1].length() : 0;
							if (MathUtils.isNearlyEqual(element, numberValue.getDouble(), Math.pow(10, dec))) {
								inList = true;
								break;
							}
						}
					}
					if (inList) {
						return false;
					} else {
						continue;
					}
				}
				if (attributeType == TaskAttributeType.FLAG) {
					FlagArrayValue list = (FlagArrayValue) valueComp;
					FlagValue flagValue = (FlagValue) valueTask;
					boolean inList = false;
					for (TaskFlag flag : list.getFlags()) {
						if (flag == flagValue.getFlag()) {
							inList = true;
							break;
						}
					}
					if (inList) {
						return false;
					} else {
						continue;
					}
				}
			}

			if (comp == FilterCriteria.ComparisonOp.CONTAINS) {
				String strComp = ((TextValue) valueComp).getText();

				if (attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.DESCRIPTION || attributeType == TaskAttributeType.TEXT) {
					String strTask = ((TextValue) valueTask).getText();
					if (strTask.contains(strComp)) {
						continue;
					} else {
						return false;
					}
				}

				if (attributeType == TaskAttributeType.FLAG) {
					String strTask = ((FlagValue) valueTask).getFlag().name;
					if (strTask.contains(strComp)) {
						continue;
					} else {
						return false;
					}
				}

			}

			if (comp == FilterCriteria.ComparisonOp.CONTAINS_NOT) {
				String strComp = ((TextValue) valueComp).getText();

				if (attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.DESCRIPTION || attributeType == TaskAttributeType.TEXT) {
					String strTask = ((TextValue) valueTask).getText();
					if (strTask.contains(strComp)) {
						return false;
					} else {
						continue;
					}
				}

				if (attributeType == TaskAttributeType.FLAG) {
					String strTask = ((FlagValue) valueTask).getFlag().name;
					if (strTask.contains(strComp)) {
						if (strTask.contains(strComp)) {
							return false;
						} else {
							continue;
						}
					}
				}

			}

		}

		return true;
	}




	private boolean inTextArray(TextArrayValue array, TextValue value) {
		for (String listElement : array.getText()) {
			if (listElement.equals(value.getText())) {
				return true;
			}
		}
		return false;
	}




	private boolean inTextArray(TextArrayValue array, NumberValue value) {
		for (String listElement : array.getText()) {
			if (value.isInt()) {
				int element = Integer.parseInt(listElement);
				if (element == value.getInt()) {
					return true;
				}
			} else {
				double element = Double.parseDouble(listElement);
				int dec = listElement.contains(".") ? listElement.split(".")[1].length() : 0;
				if (MathUtils.isNearlyEqual(element, value.getDouble(), Math.pow(10, dec))) {
					return true;
				}
			}
		}
		return false;
	}




	private boolean inFlagArray(FlagArrayValue array, FlagValue value) {
		for (TaskFlag flag : array.getFlags()) {
			if (flag == value.getFlag()) {
				return true;
			}
		}
		return false;
	}


	//======================//
	//        GETTER        //
	//======================//




	public void getFilteredTasks(Request request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.onResponse(new Response<>(Response.State.SUCCESS, project.filteredTasks));
		}
	}




	public void getFilterCriteria(Request request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.onResponse(new Response<>(Response.State.SUCCESS, project.filterCriteria));
		}
	}


	//======================//
	//        SETTER        //
	//======================//




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




	/**
	 * Applies the current filter-criterias to all tasks and saves the result in a new list (Project.filteredTasks)<p>
	 * Events:<p>
	 * -FilteredTasksChangedEvent: when the list of filtered tasks has changed
	 */
	public void applyFilters() {
		Project project = Logic.project.getProject();
		if (project != null) {

			List<FilterCriteria> filters = project.filterCriteria;
			List<Task> allTasks = project.tasks;
			List<Task> filtered = new ArrayList<>();

			for (int i = 0, n = allTasks.size(); i < n; i++) {
				Task task = allTasks.get(i);
				boolean match = match(task, filters);
				if (match) {
					filtered.add(task);
				}
			}

			project.filteredTasks = filtered;
			EventManager.fireEvent(new FilteredTasksChangedEvent(this));

		}
	}


}
