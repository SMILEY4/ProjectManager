package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;

import java.util.ArrayList;
import java.util.List;

public class FilterLogic {


	protected FilterLogic() {

		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent)e;
				removeFilterCriteria(event.getAttribute());
			}
		}, AttributeRemovedEvent.class);

		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				rebuildFilteredList();
			}
		}, FilterCriteriaChangedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class);

	}




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




	public boolean removeFilterCriteria(TaskAttribute attribute) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			List<FilterCriteria> toRemove = new ArrayList<>();
			for(FilterCriteria element : project.filterCriteria) {
				if(element.attribute == attribute) {
					toRemove.add(element);
				}
			}
			project.filterCriteria.removeAll(toRemove);
			if(!toRemove.isEmpty()) {
				EventManager.fireEvent(new FilterCriteriaChangedEvent(project.filterCriteria, this));
			}
			return !toRemove.isEmpty();
		} else {
			return false;
		}
	}





	public boolean rebuildFilteredList() {
		final boolean allowNoValueMatch = false;

		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			List<FilterCriteria> filters = project.filterCriteria;
			List<Task> allTasks = project.tasks;
			List<Task> filtered = new ArrayList<>();

			for(int i=0, n=allTasks.size(); i<n; i++) {
				Task task = allTasks.get(i);

				boolean match = match(task, filters, allowNoValueMatch);
				if(match) {
					filtered.add(task);
				}

			}

			project.filteredTasks = filtered;
			EventManager.fireEvent(new FilteredTasksChangedEvent(this));
			return true;

		} else {
			return false;
		}
	}




	private boolean match(Task task, List<FilterCriteria> filters, boolean allowNoValueMatch) {

		for(FilterCriteria filter : filters) {

			final TaskAttribute attribute = filter.attribute;
			final TaskAttributeType attributeType = attribute.data.getType();
			final FilterCriteria.ComparisonOp comp = filter.comparisonOp;

			final TaskAttributeValue valueComp = filter.comparisionValue;
			final TaskAttributeValue valueTask = Logic.tasks.getAttributeValue(task, attribute.name);

			if(!allowNoValueMatch && valueTask instanceof NoValue) {
				return false;
			}

			if(valueTask.getClass() != valueComp.getClass()) {
				return false;
			}


			if(comp == FilterCriteria.ComparisonOp.EQUALITY) {
				if(valueTask.compareTo(valueComp) == 0) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.INEQUALITY) {
				if(valueTask.compareTo(valueComp) != 0) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.LESS_THAN) {
				if(valueTask.compareTo(valueComp) == -1) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.LESS_THAN_EQUAL) {
				if(valueTask.compareTo(valueComp) != +1) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.GREATER_THAN) {
				if(valueTask.compareTo(valueComp) == +1) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.GREATER_THAN_EQUAL) {
				if(valueTask.compareTo(valueComp) != -1) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.IN_RANGE) {
				NumberValue value = (NumberValue)valueTask;
				NumberPairValue range = (NumberPairValue)valueComp;
				if(range.inRange(value)) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.NOT_IN_RANGE) {
				NumberValue value = (NumberValue)valueTask;
				NumberPairValue range = (NumberPairValue)valueComp;
				if(!range.inRange(value)) {
					continue;
				} else {
					return false;
				}
			}

			if(comp == FilterCriteria.ComparisonOp.IN_LIST) {
				if(attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.TEXT) {
					TextArrayValue list = (TextArrayValue)valueComp;
					TextValue textValue = (TextValue)valueTask;
					boolean inList = false;
					for(String listElement : list.getText()) {
						if(listElement.equals(textValue)) {
							inList = true;
							break;
						}
					}
					if(inList) {
						continue;
					} else {
						return false;
					}
				}
				if(attributeType == TaskAttributeType.ID || attributeType == TaskAttributeType.NUMBER) {
					TextArrayValue list = (TextArrayValue)valueComp;
					NumberValue numberValue = (NumberValue)valueTask;
					boolean inList = false;
					for(String listElement : list.getText()) {
						if(numberValue.isInt()) {
							int element = Integer.parseInt(listElement);
							if(element == numberValue.getInt()) {
								inList = true;
								break;
							}
						} else {
							double element = Double.parseDouble(listElement);
							int dec = listElement.contains(".") ? listElement.split(".")[1].length() : 0;
							if(MathUtils.isNearlyEqual(element, numberValue.getDouble(), Math.pow(10,dec))) {
								inList = true;
								break;
							}
						}
					}
					if(inList) {
						continue;
					} else {
						return false;
					}
				}
				if(attributeType == TaskAttributeType.FLAG) {
					FlagArrayValue list = (FlagArrayValue)valueComp;
					FlagValue flagValue = (FlagValue)valueTask;
					boolean inList = false;
					for(TaskFlag flag : list.getFlags()) {
						if(flag == flagValue.getFlag()) {
							inList = true;
							break;
						}
					}
					if(inList) {
						continue;
					} else {
						return false;
					}
				}
			}

			if(comp == FilterCriteria.ComparisonOp.NOT_IN_LIST) {
				if(attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.TEXT) {
					TextArrayValue list = (TextArrayValue)valueComp;
					TextValue textValue = (TextValue)valueTask;
					boolean inList = false;
					for(String listElement : list.getText()) {
						if(listElement.equals(textValue)) {
							inList = true;
							break;
						}
					}
					if(inList) {
						return false;
					} else {
						continue;
					}
				}
				if(attributeType == TaskAttributeType.ID || attributeType == TaskAttributeType.NUMBER) {
					TextArrayValue list = (TextArrayValue)valueComp;
					NumberValue numberValue = (NumberValue)valueTask;
					boolean inList = false;
					for(String listElement : list.getText()) {
						if(numberValue.isInt()) {
							int element = Integer.parseInt(listElement);
							if(element == numberValue.getInt()) {
								inList = true;
								break;
							}
						} else {
							double element = Double.parseDouble(listElement);
							int dec = listElement.contains(".") ? listElement.split(".")[1].length() : 0;
							if(MathUtils.isNearlyEqual(element, numberValue.getDouble(), Math.pow(10,dec))) {
								inList = true;
								break;
							}
						}
					}
					if(inList) {
						return false;
					} else {
						continue;
					}
				}
				if(attributeType == TaskAttributeType.FLAG) {
					FlagArrayValue list = (FlagArrayValue)valueComp;
					FlagValue flagValue = (FlagValue)valueTask;
					boolean inList = false;
					for(TaskFlag flag : list.getFlags()) {
						if(flag == flagValue.getFlag()) {
							inList = true;
							break;
						}
					}
					if(inList) {
						return false;
					} else {
						continue;
					}
				}
			}

			if(comp == FilterCriteria.ComparisonOp.CONTAINS) {
				String strComp = ((TextValue)valueComp).getText();

				if(attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.DESCRIPTION || attributeType == TaskAttributeType.TEXT) {
					String strTask = ((TextValue)valueTask).getText();
					if(strTask.contains(strComp)) {
						continue;
					} else {
						return false;
					}
				}

				if(attributeType == TaskAttributeType.FLAG) {
					String strTask = ((FlagValue)valueTask).getFlag().name;
					if(strTask.contains(strComp)) {
						continue;
					} else {
						return false;
					}
				}

			}

			if(comp == FilterCriteria.ComparisonOp.CONTAINS_NOT) {
				String strComp = ((TextValue)valueComp).getText();

				if(attributeType == TaskAttributeType.CHOICE || attributeType == TaskAttributeType.DESCRIPTION || attributeType == TaskAttributeType.TEXT) {
					String strTask = ((TextValue)valueTask).getText();
					if(strTask.contains(strComp)) {
						return false;
					} else {
						continue;
					}
				}

				if(attributeType == TaskAttributeType.FLAG) {
					String strTask = ((FlagValue)valueTask).getFlag().name;
					if(strTask.contains(strComp)) {
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




}
