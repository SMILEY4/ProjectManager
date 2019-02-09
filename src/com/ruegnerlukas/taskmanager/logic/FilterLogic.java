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

			final TaskAttribute attribute = filter.attribute;
			final TaskAttributeType attributeType = attribute.data.getType();
			final FilterCriteria.ComparisonOp comp = filter.comparisonOp;

			final TaskAttributeValue valueComp = filter.comparisionValue;
			final TaskAttributeValue valueTask = Logic.tasks.getValue(task, attribute);

			if (valueTask instanceof NoValue) {
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
