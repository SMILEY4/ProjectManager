package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.events.TaskValueChangeEvent;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;

import java.time.LocalDateTime;
import java.util.*;

public class TaskLogic {


	public static void init() {

		Data.projectProperty.addListener(((observable, oldValue, newProject) -> {
			if (newProject != null) {

				// add listener to "tasks"
				// -> task removed -> remove task from dependencies
				newProject.data.tasks.addListener((ListChangeListener<Task>) c -> {
					while (c.next()) {
						if (c.wasRemoved()) {
							for (Task task : c.getRemoved()) {
								removeFromDependencies(Data.projectProperty.get(), task);
							}
						}
					}
				});

				// add listeners to "taskattributes"
				// -> attr. removed -> update filter/group/sort data
				newProject.data.attributes.addListener((ListChangeListener<TaskAttribute>) c -> {
					while (c.next()) {
						if (c.wasRemoved()) {
							for (TaskAttribute attribute : c.getRemoved()) {
								if (newProject.data.filterData.get() != null) {
									FilterCriteria criteria = newProject.data.filterData.get();
									if (criteria.type == FilterCriteria.CriteriaType.TERMINAL
											&& ((TerminalFilterCriteria) criteria).attribute == attribute) {
										setFilter(newProject, null, null);
									} else {
										FilterCriteria newCriteria = removeAttributeFromFilterCriteria(criteria, attribute);
										if (newCriteria != criteria) {
											setFilter(newProject, newCriteria, null);
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
											List<SortElement> newList = new ArrayList<>();
											newList.addAll(newProject.data.sortData.get().sortElements);
											newList.remove(element);
											SortData newData = new SortData(newList);
											if (newData.sortElements.isEmpty()) {
												setSortData(newProject, null, null);
											} else {
												newProject.data.sortData.set(newData);
											}
											newProject.temporaryData.lastGroupsValid.set(false);
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




	public static FilterCriteria removeAttributeFromFilterCriteria(FilterCriteria criteria, TaskAttribute attribute) {


		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			AndFilterCriteria andCriteria = (AndFilterCriteria) criteria;
			List<FilterCriteria> list = new ArrayList<>();
			for (FilterCriteria c : andCriteria.subCriteria) {
				FilterCriteria child = removeAttributeFromFilterCriteria(c, attribute);
				if (child != null) {
					list.add(child);
				}
			}
			if (list.size() != andCriteria.subCriteria.size()) {
				return new AndFilterCriteria(list);
			} else {
				return criteria;
			}
		}


		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			OrFilterCriteria orCriteria = (OrFilterCriteria) criteria;
			List<FilterCriteria> list = new ArrayList<>();
			for (FilterCriteria c : orCriteria.subCriteria) {
				FilterCriteria child = removeAttributeFromFilterCriteria(c, attribute);
				if (child != null) {
					list.add(child);
				}
			}
			if (list.size() != orCriteria.subCriteria.size()) {
				return new OrFilterCriteria(list);
			} else {
				return criteria;
			}
		}


		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			TerminalFilterCriteria terminal = (TerminalFilterCriteria) criteria;
			if (terminal.attribute == attribute) {
				return null;
			} else {
				return criteria;
			}
		}

		return null;
	}




	@SuppressWarnings ("Duplicates")
//	private static int removeAttributeFromFilterCriteria(FilterCriteria criteria, TaskAttribute attribute) {
//		int nRemoved = 0;
//		if (criteria.type == FilterCriteria.CriteriaType.AND) {
//			AndFilterCriteria andCriteria = (AndFilterCriteria) criteria;
//			List<FilterCriteria> toRemove = new ArrayList<>();
//			for (int i = 0; i < andCriteria.subCriteria.size(); i++) {
//				FilterCriteria child = andCriteria.subCriteria.get(i);
//				if (child.type == FilterCriteria.CriteriaType.TERMINAL && ((TerminalFilterCriteria) child).attribute == attribute) {
//					toRemove.add(child);
//				} else if (child.type != FilterCriteria.CriteriaType.TERMINAL) {
//					nRemoved += removeAttributeFromFilterCriteria(child, attribute);
//				}
//			}
//			andCriteria.subCriteria.removeAll(toRemove);
//			nRemoved += toRemove.size();
//		}
//		if (criteria.type == FilterCriteria.CriteriaType.OR) {
//			OrFilterCriteria orCriteria = (OrFilterCriteria) criteria;
//			List<FilterCriteria> toRemove = new ArrayList<>();
//			for (int i = 0; i < orCriteria.subCriteria.size(); i++) {
//				FilterCriteria child = orCriteria.subCriteria.get(i);
//				if (child.type == FilterCriteria.CriteriaType.TERMINAL && ((TerminalFilterCriteria) child).attribute == attribute) {
//					toRemove.add(child);
//				} else if (child.type != FilterCriteria.CriteriaType.TERMINAL) {
//					nRemoved += removeAttributeFromFilterCriteria(child, attribute);
//				}
//			}
//			orCriteria.subCriteria.removeAll(toRemove);
//			nRemoved += toRemove.size();
//		}
//		return nRemoved;
//	}


	private static void removeFromDependencies(Project project, Task task) {
		// remove from dependencies
		List<TaskAttribute> dependencyAttributes = AttributeLogic.findAttributes(project, AttributeType.DEPENDENCY);
		for (int i = 0, n = project.data.tasks.size(); i < n; i++) {
			Task t = project.data.tasks.get(i);
			for (TaskAttribute attribute : dependencyAttributes) {
				TaskValue<?> taskValue = TaskLogic.getValueOrDefault(t, attribute);
				if (taskValue.getAttType() == AttributeType.DEPENDENCY) {
					DependencyValue value = (DependencyValue) taskValue;
					Set<Task> dependencies = new HashSet<>(Arrays.asList(value.getValue()));
					if (dependencies.contains(task)) {
						if (dependencies.size() == 1) {
							TaskLogic.setValue(project, t, attribute, new NoValue());
						} else {
							Task[] array = new Task[dependencies.size() - 1];
							for (int j = 0, k = 0; j < dependencies.size(); j++) {
								if (value.getValue()[j] != task) {
									array[k++] = value.getValue()[j];
								}
							}
							TaskLogic.setValue(project, t, attribute, new DependencyValue(array));
						}
					}
				}
			}
		}
	}




	public static Task createTask(Project project) {

		// get id
		final int id = project.settings.idCounter.get();
		project.settings.idCounter.set(id + 1);

		// get time
		LocalDateTime time = LocalDateTime.now();

		// create task
		Task task = new Task(id, project, project.dataHandler);

		// set date created
		TaskAttribute createdAttribute = AttributeLogic.findAttribute(project, AttributeType.CREATED);
		setValue(project, task, createdAttribute, new CreatedValue(time));

		// set last updated
		TaskAttribute updatedAttribute = AttributeLogic.findAttribute(project, AttributeType.LAST_UPDATED);
		setValue(project, task, updatedAttribute, new LastUpdatedValue(time));

		return task;
	}




	public static void deleteTask(Project project, Task task) {
		project.data.tasks.remove(task);
	}




	public static Task findTaskByID(Project project, int id) {
		TaskAttribute idAttribute = AttributeLogic.findAttribute(project, AttributeType.ID);
		if (idAttribute == null) {
			return null;
		}
		for (Task task : project.data.tasks) {
			final int idTask = ((IDValue) getTaskValue(task, idAttribute)).getValue();
			if (idTask == id) {
				return task;
			}
		}
		return null;
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
		TaskValue<?> value = task.values.get(attribute);
		if (value == null) {
			return new NoValue();
		} else {
			return value;
		}
	}




	public static boolean setValue(Project project, Task task, TaskAttribute attribute, TaskValue<?> value) {

		// validate value
		if (!AttributeLogic.LOGIC_MODULES.get(attribute.type.get()).isValidTaskValue(attribute, value == null ? new NoValue() : value)) {
			Logger.get().debug("Failed to set task value: " + attribute.name.get() + " - invalid value: " + value + (value != null ? "." + value.getValue() : ""));
			return false;
		}

		// set value
		TaskValue<?> prevValue = task.values.get(attribute);
		if (value == null) {
			task.values.remove(attribute);
		} else {
			task.values.put(attribute, value);
		}
		onTaskValueChanged(project, task, attribute, prevValue, value);

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




	private static void onTaskValueChanged(Project project, Task task, TaskAttribute attribute, TaskValue<?> prevValue, TaskValue<?> newValue) {

		// fire event
		TaskValueChangeEvent event = new TaskValueChangeEvent(task, attribute, prevValue, newValue);
		for (EventHandler<TaskValueChangeEvent> handler : valueChangedHandlers) {
			handler.handle(event);
		}

		// update "last_changed"
		if (attribute.type.get() != AttributeType.LAST_UPDATED && newValue != prevValue) {
			if (prevValue != null && prevValue.compare(newValue) != 0) {
				setValue(project, task, AttributeLogic.findAttribute(project, AttributeType.LAST_UPDATED), new LastUpdatedValue(LocalDateTime.now()));
			}
		}
	}




	protected static List<TaskAttribute> getUsedFilterAttributes(FilterCriteria criteria) {
		List<TaskAttribute> attributes = new ArrayList<>();
		getUsedFilterAttributes(criteria, attributes);
		return attributes;
	}




	private static void getUsedFilterAttributes(FilterCriteria criteria, List<TaskAttribute> attributes) {
		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			attributes.add(((TerminalFilterCriteria) criteria).attribute);
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
		project.data.presetSelectedFilter.set(preset);
	}




	public static void setGroupData(Project project, TaskGroupData groupData, String preset) {
		if (preset == null && groupData != null && groupData.attributes.isEmpty() && groupData.customHeaderString == null) {
			project.data.groupData.set(null);
			project.data.presetSelectedGroup.set(null);
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
			project.data.presetSelectedGroup.set(preset);
		}

	}




	public static void setSortData(Project project, SortData sortData, String preset) {
		if (preset == null && sortData != null && sortData.sortElements.isEmpty()) {
			project.data.sortData.set(null);
			project.data.presetSelectedSort.set(null);
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
				if (!toRemove.isEmpty()) {
					List<SortElement> newList = new ArrayList<>();
					newList.addAll(sortData.sortElements);
					newList.removeAll(toRemove);
					sortData = new SortData(newList);
				}
			}

			project.data.sortData.set(sortData);
			project.data.presetSelectedSort.set(preset);
		}

	}


}
