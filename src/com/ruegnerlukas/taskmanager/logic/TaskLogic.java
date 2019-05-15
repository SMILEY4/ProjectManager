package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogicManager;
import com.ruegnerlukas.taskmanager.logic.events.TaskValueChangeEvent;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;

import java.time.LocalDateTime;
import java.util.*;

public class TaskLogic {


	public static void init() {

		Data.projectProperty.addListener(((observable, oldValue, newProject) -> {
			if (newProject != null) {

				// add listeners to "remove taskattribute"
				newProject.data.attributes.addListener((ListChangeListener<TaskAttribute>) c -> {
					while (c.next()) {
						if (c.wasRemoved()) {
							for (TaskAttribute attribute : c.getRemoved()) {
								if (newProject.data.filterData.get() != null) {
									FilterCriteria criteria = newProject.data.filterData.get();
									if (criteria.type == FilterCriteria.CriteriaType.TERMINAL
											&& ((TerminalFilterCriteria) criteria).attribute.get() == attribute) {
										setFilter(newProject, null, null);
									} else {
										int nRemoved = removeAttributeFromFilterCriteria(criteria, attribute);
										if (nRemoved > 0) {
											newProject.temporaryData.lastGroupsValid.set(false);
										}
									}

								}
								if (newProject.data.groupData.get() != null && newProject.data.groupData.get().attributes.contains(attribute)) {
									if (newProject.data.groupData.get().attributes.remove(attribute)) {
										newProject.temporaryData.lastGroupsValid.set(false);
										if (newProject.data.groupData.get().attributes.isEmpty()) {
											setGroupData(newProject, null, null);
										}
									}
								}
								if (newProject.data.sortData.get() != null) {
									for (int i = 0; i < newProject.data.sortData.get().sortElements.size(); i++) {
										SortElement element = newProject.data.sortData.get().sortElements.get(i);
										if (element.attribute.get() == attribute) {
											newProject.data.sortData.get().sortElements.remove(element);
											newProject.temporaryData.lastGroupsValid.set(false);
											if (newProject.data.sortData.get().sortElements.isEmpty()) {
												setSortData(newProject, null, null);
											}
											break;
										}
									}
								}
							}
						}
					}
				});

			}
		}));
	}




	@SuppressWarnings ("Duplicates")
	private static int removeAttributeFromFilterCriteria(FilterCriteria criteria, TaskAttribute attribute) {
		int nRemoved = 0;
		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			AndFilterCriteria andCriteria = (AndFilterCriteria) criteria;
			List<FilterCriteria> toRemove = new ArrayList<>();
			for (int i = 0; i < andCriteria.subCriteria.size(); i++) {
				FilterCriteria child = andCriteria.subCriteria.get(i);
				if (child.type == FilterCriteria.CriteriaType.TERMINAL && ((TerminalFilterCriteria) child).attribute.get() == attribute) {
					toRemove.add(child);
				} else if (child.type != FilterCriteria.CriteriaType.TERMINAL) {
					nRemoved += removeAttributeFromFilterCriteria(child, attribute);
				}
			}
			andCriteria.subCriteria.removeAll(toRemove);
			nRemoved += toRemove.size();
		}
		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			OrFilterCriteria orCriteria = (OrFilterCriteria) criteria;
			List<FilterCriteria> toRemove = new ArrayList<>();
			for (int i = 0; i < orCriteria.subCriteria.size(); i++) {
				FilterCriteria child = orCriteria.subCriteria.get(i);
				if (child.type == FilterCriteria.CriteriaType.TERMINAL && ((TerminalFilterCriteria) child).attribute.get() == attribute) {
					toRemove.add(child);
				} else if (child.type != FilterCriteria.CriteriaType.TERMINAL) {
					nRemoved += removeAttributeFromFilterCriteria(child, attribute);
				}
			}
			orCriteria.subCriteria.removeAll(toRemove);
			nRemoved += toRemove.size();
		}
		return nRemoved;
	}




	public static Task createTask(Project project) {
		Task task = new Task();

		LocalDateTime time = LocalDateTime.now();

		// set id
		TaskAttribute idAttribute = AttributeLogic.findAttribute(project, AttributeType.ID);
		final int id = project.settings.idCounter.get();
		project.settings.idCounter.set(id + 1);
		setValue(project, task, idAttribute, new IDValue(id));

		// set date created
		TaskAttribute createdAttribute = AttributeLogic.findAttribute(project, AttributeType.CREATED);
		setValue(project, task, createdAttribute, new CreatedValue(time));

		// set last updated
		TaskAttribute updatedAttribute = AttributeLogic.findAttribute(project, AttributeType.LAST_UPDATED);
		setValue(project, task, updatedAttribute, new LastUpdatedValue(time));

		// TEMP

		// set random number
		double randNumber = new Random().nextInt(5);
		TaskAttribute numberAttribute = AttributeLogic.findAttribute(project, AttributeType.NUMBER);

		if (numberAttribute != null) {
			setValue(project, task, numberAttribute, new NumberValue(randNumber));

			TaskAttribute descriptionAttribute = AttributeLogic.findAttribute(project, AttributeType.DESCRIPTION);
			setValue(project, task, descriptionAttribute, new DescriptionValue("Some Text " + randNumber));
		}


		return task;
	}




	public static TaskValue getValueOrDefault(Task task, TaskAttribute attribute) {
		TaskValue<?> trueValue = getTaskValue(task, attribute);
		if (trueValue.getAttType() != attribute.type.get()) {
			if (AttributeLogic.getUsesDefault(attribute)) {
				return AttributeLogic.getDefaultValue(attribute);
			} else {
				return new NoValue();
			}
		} else {
			return trueValue;
		}
	}




	public static TaskValue<?> getTaskValue(Task task, TaskAttribute attribute) {
		TaskValue<?> value = task.attributes.get(attribute);
		if (value == null) {
			return new NoValue();
		} else {
			return value;
		}
	}




	public static boolean setValue(Project project, Task task, TaskAttribute attribute, TaskValue<?> value) {

		// validate value
		if (!AttributeLogicManager.isValidTaskValue(attribute, value == null ? new NoValue() : value)) {
			Logger.get().debug("Failed to set task value: " + attribute.name.get() + " - invalid value: " + value + (value != null ? "." + value.getValue() : ""));
			return false;
		}

		// set value
		TaskValue<?> prevValue = task.attributes.get(attribute);
		if(value == null) {
			task.attributes.remove(attribute);
		} else {
			task.attributes.put(attribute, value);
		}
		onTaskValueChanged(task, attribute, prevValue, value);

		// check/update displayed tasks
		boolean modifiedDisplay = false;

		// check filter data
		if (!modifiedDisplay && project.data.filterData.get() != null) {
			FilterCriteria filterData = project.data.filterData.get();
			if (getUsedFilterAttributes(filterData).contains(attribute)) {
				modifiedDisplay = true;
			}
		}

		// check group data
		if (!modifiedDisplay && project.data.groupData.get() != null) {
			TaskGroupData groupData = project.data.groupData.get();
			if (groupData.attributes.contains(attribute)) {
				modifiedDisplay = true;
			}
		}

		// check sort data
		if (!modifiedDisplay && project.data.sortData.get() != null) {
			SortData sortData = project.data.sortData.get();
			for (SortElement element : sortData.sortElements) {
				if (element.attribute.get() == attribute) {
					modifiedDisplay = true;
					break;
				}
			}
		}

		if (modifiedDisplay) {
			TaskDisplayLogic.onTaskModified(project, task, attribute);
		}

		return true;
	}




	private static final List<EventHandler<TaskValueChangeEvent>> valueChangedHandlers = new ArrayList<>();




	public static void addOnTaskValueChanged(EventHandler<TaskValueChangeEvent> handler) {
		valueChangedHandlers.add(handler);
	}




	public static void removeOnTaskValueChanged(EventHandler<TaskValueChangeEvent> handler) {
		valueChangedHandlers.remove(handler);
	}




	private static void onTaskValueChanged(Task task, TaskAttribute attribute, TaskValue<?> prevValue, TaskValue<?> newValue) {
		TaskValueChangeEvent event = new TaskValueChangeEvent(task, attribute, prevValue, newValue);
		for (EventHandler<TaskValueChangeEvent> handler : valueChangedHandlers) {
			handler.handle(event);
		}
	}




	protected static List<TaskAttribute> getUsedFilterAttributes(FilterCriteria criteria) {
		List<TaskAttribute> attributes = new ArrayList<>();
		getUsedFilterAttributes(criteria, attributes);
		return attributes;
	}




	private static void getUsedFilterAttributes(FilterCriteria criteria, List<TaskAttribute> attributes) {
		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			attributes.add(((TerminalFilterCriteria) criteria).attribute.get());
		}
		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			for (FilterCriteria child : ((AndFilterCriteria) criteria).subCriteria) {
				getUsedFilterAttributes(child, attributes);
			}
		}
		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			for (FilterCriteria child : ((OrFilterCriteria) criteria).subCriteria) {
				getUsedFilterAttributes(child, attributes);
			}
		}
	}




	public static void setFilter(Project project, FilterCriteria criteria, String preset) {
		project.data.filterData.set(criteria);
		project.data.selectedFilterPreset.set(preset);
	}




	public static void setGroupData(Project project, TaskGroupData groupData, String preset) {
		if (preset == null && groupData != null && groupData.attributes.isEmpty() && groupData.customHeaderString.get() == null) {
			project.data.groupData.set(null);
			project.data.selectedGroupPreset.set(null);
		} else {

			// remove duplicates
			if (groupData != null) {
				Set<TaskAttribute> attributes = new HashSet<>();
				Iterator<TaskAttribute> iter = groupData.attributes.iterator();
				while (iter.hasNext()) {
					TaskAttribute a = iter.next();
					if (attributes.contains(a)) {
						iter.remove();
					} else {
						attributes.add(a);
					}
				}
			}

			project.data.groupData.set(groupData);
			project.data.selectedGroupPreset.set(preset);
		}

	}




	public static void setSortData(Project project, SortData sortData, String preset) {
		if (preset == null && sortData != null && sortData.sortElements.isEmpty()) {
			project.data.sortData.set(null);
			project.data.selectedSortPreset.set(null);
		} else {

			// remove duplicates
			if (sortData != null) {
				List<SortElement> toRemove = new ArrayList<>();
				Set<TaskAttribute> attributes = new HashSet<>();
				for (SortElement e : sortData.sortElements) {
					if (attributes.contains(e.attribute.get())) {
						toRemove.add(e);
					} else {
						attributes.add(e.attribute.get());
					}
				}
				sortData.sortElements.removeAll(toRemove);
			}

			project.data.sortData.set(sortData);
			project.data.selectedSortPreset.set(preset);
		}

	}


}
