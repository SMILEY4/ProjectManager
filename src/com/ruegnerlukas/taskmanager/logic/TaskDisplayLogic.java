package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.*;

public class TaskDisplayLogic {


	public static void init() {

		Data.projectProperty.addListener(((observable, oldValue, newProject) -> {
			if (newProject != null) {

				// add listener to tasks
				newProject.data.tasks.addListener((ListChangeListener<Task>) c -> {
					while (c.next()) {
						if (c.wasAdded()) {
							for (Task task : c.getAddedSubList()) {
								onTaskAdded(Data.projectProperty.get(), task);
							}
						}
						if (c.wasRemoved()) {
							for (Task task : c.getRemoved()) {
								onTaskRemoved(Data.projectProperty.get(), task);
							}
						}
					}
				});

				// add listeners to filter/group/sort-data -> invalidate last groups on change
				newProject.data.filterData.addListener(((observable1, oldValue1, newValue1) -> {
					Data.projectProperty.get().temporaryData.lastGroupsValid.set(false);
				}));
				newProject.data.groupData.addListener(((observable1, oldValue1, newValue1) -> {
					Data.projectProperty.get().temporaryData.lastGroupsValid.set(false);
				}));
				newProject.data.sortData.addListener(((observable1, oldValue1, newValue1) -> {
					Data.projectProperty.get().temporaryData.lastGroupsValid.set(false);
				}));

			}
		}));
	}




	/**
	 * Call method when the given {@link Task} was added the given {@link Project}
	 */
	private static void onTaskAdded(Project project, Task task) {
		if (project.temporaryData.lastGroupsValid.get()) {

			// check if task passes filter and will be displayed
			if (project.data.filterData.get() != null) {
				FilterNode rootFilter = new FilterNode(project.data.filterData.get());
				rootFilter.expand();
				if (!rootFilter.matches(task)) {
					return;
				}
			}

			// find group for task
			TaskGroup taskGroup = null;

			if (project.data.groupData.get() == null && project.temporaryData.lastTaskGroups.size() == 1) {
				// only one group exists
				taskGroup = project.temporaryData.lastTaskGroups.get(0);

			} else {
				// compare values of added task to values of a task in the current group -> when matching -> found a group
				outer:
				for (TaskGroup group : project.temporaryData.lastTaskGroups) {
					Task taskRef = group.tasks.get(0);

					for (TaskAttribute attribute : group.attributes) {
						TaskValue<?> valueRef = TaskLogic.getValueOrDefault(taskRef, attribute);
						TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, attribute);
						if (valueRef.compare(valueTask) != 0) {
							continue outer;
						}
					}

					taskGroup = group;
					break;
				}
			}

			if (taskGroup == null) {
				// no group found -> create new group with added task
				taskGroup = new TaskGroup();
				taskGroup.tasks.add(task);
				taskGroup.attributes.setAll(project.data.groupData.get() == null ? new ArrayList<>() : project.data.groupData.get().attributes);
				project.temporaryData.lastTaskGroups.add(taskGroup);

			} else {
				// add task to group
				taskGroup.tasks.add(task);
				if (project.data.sortData.get() != null) {
					sortTasks(taskGroup.tasks, project.data.sortData.get().sortElements);
				}
			}

			// sort groups
			sortGroups(project.temporaryData.lastTaskGroups, taskGroup.attributes);

			// notify listeners
			callListenersDisplayChanged(project);
		}

	}




	/**
	 * Call method when the given {@link Task} was removed from the given {@link Project}
	 */
	private static void onTaskRemoved(Project project, Task task) {

		// check if groups are still valid (ignore if invalid, will change anyways)
		if (project.temporaryData.lastGroupsValid.get()) {

			// find group containing removed task
			TaskGroup taskGroup = null;
			for (TaskGroup group : project.temporaryData.lastTaskGroups) {
				if (group.tasks.contains(task)) {
					taskGroup = group;
					break;
				}
			}

			// remove task from group and notify listeners
			if (taskGroup != null) {
				taskGroup.tasks.remove(task);
				if (taskGroup.tasks.isEmpty()) {
					project.temporaryData.lastTaskGroups.remove(taskGroup);
				}
				callListenersDisplayChanged(project);
			}
		}
	}




	/**
	 * Call method when a value of the given {@link Task} in the given {@link Project} was modified
	 */
	public static void onTaskModified(Project project, Task task) {
		// refresh by removing task from groups and adding it again
		if (project.data.tasks.contains(task)) {
			onTaskRemoved(project, task);
			onTaskAdded(project, task);
		}
	}




	/**
	 * Creates {@link TaskGroup}s for the given {@link Project}. (Reuses last groups if still valid)
	 */
	public static List<TaskGroup> createTaskGroups(Project project) {
		return createTaskGroups(project, false);
	}




	/**
	 * Creates {@link TaskGroup}s for the given {@link Project}. (can reuse last groups if still valid)
	 *
	 * @param force true, if last groups should not be reused
	 */
	public static List<TaskGroup> createTaskGroups(Project project, boolean force) {
		if (project.temporaryData.lastGroupsValid.get() && !force) {
			return project.temporaryData.lastTaskGroups;
		} else {
			final List<Task> tasks = project.data.tasks;
			final FilterCriteria dataFilter = project.data.filterData.get();
			final TaskGroupData dataGroup = project.data.groupData.get();
			final SortData dataSort = project.data.sortData.get();
			List<TaskGroup> taskGroups = createTaskGroups(tasks, dataFilter, dataGroup, dataSort);
			project.temporaryData.lastTaskGroups.setAll(taskGroups);
			project.temporaryData.lastGroupsValid.set(true);
			callListenersDisplayChanged(project);
			return taskGroups;
		}
	}




	/**
	 * creates the {@link TaskGroup}s with the given data
	 */
	private static List<TaskGroup> createTaskGroups(List<Task> tasks, FilterCriteria dataFilter, TaskGroupData dataGroup, SortData dataSort) {

		// 1. popupfilter
		List<Task> filteredTasks = new ArrayList<>();

		if (dataFilter == null) {
			filteredTasks.addAll(tasks);

		} else {
			FilterNode rootFilter = new FilterNode(dataFilter);
			rootFilter.expand();

			for (int i = 0, n = tasks.size(); i < n; i++) {
				Task task = tasks.get(i);
				if (rootFilter.matches(task)) {
					filteredTasks.add(task);
				}
			}
		}


		// 2. groups
		List<TaskGroup> groups = new ArrayList<>();
		List<TaskAttribute> groupAttributes = (dataGroup == null ? new ArrayList<>() : dataGroup.attributes);

		TaskGroup root = new TaskGroup();
		root.tasks.addAll(filteredTasks);
		groups.add(root);

		for (TaskAttribute attribute : groupAttributes) {
			List<TaskGroup> newGroups = new ArrayList<>();
			for (TaskGroup group : groups) {
				newGroups.addAll(splitTaskGroup(group, attribute));
			}
			groups.clear();
			groups.addAll(newGroups);
		}

		sortGroups(groups, groupAttributes);


		// 3. sort
		if (dataSort != null) {
			for (TaskGroup group : groups) {
				sortTasks(group.tasks, dataSort.sortElements);
			}
		}

		return groups;
	}




	/**
	 * Creates new {@link TaskGroup}s with the task of the given group, separated by the given {@link TaskAttribute}
	 */
	private static List<TaskGroup> splitTaskGroup(TaskGroup group, TaskAttribute attribute) {

		// sort projectdata into buckets
		Map<TaskValue, List<Task>> buckets = new HashMap<>();
		for (int i = 0, n = group.tasks.size(); i < n; i++) {
			Task task = group.tasks.get(i);
			TaskValue value = TaskLogic.getValueOrDefault(task, attribute);

			if (!buckets.containsKey(value)) {
				buckets.put(value, new ArrayList<>());
			}
			buckets.get(value).add(task);
		}

		// convert buckets to groups
		List<TaskGroup> groups = new ArrayList<>();
		for (TaskValue value : buckets.keySet()) {

			List<Task> tasks = buckets.get(value);
			if (tasks != null && !tasks.isEmpty()) {
				TaskGroup groupBucket = new TaskGroup();
				groupBucket.attributes.addAll(group.attributes);
				groupBucket.attributes.add(attribute);
				groupBucket.tasks.addAll(tasks);
				groups.add(groupBucket);
			}

		}

		return groups;
	}




	/**
	 * Sorts the given {@link TaskGroup}s by the given {@link TaskAttribute}s
	 */
	public static void sortGroups(List<TaskGroup> groups, List<TaskAttribute> attributes) {

		Comparator<TaskGroup> comparator = (groupA, groupB) -> {
			Task sampleA = groupA.tasks.get(0);
			Task sampleB = groupB.tasks.get(0);

			for (TaskAttribute attribute : attributes) {
				TaskValue<?> valueA = TaskLogic.getValueOrDefault(sampleA, attribute);
				TaskValue<?> valueB = TaskLogic.getValueOrDefault(sampleB, attribute);

				int result = valueA.compare(valueB);
				if (result != 0) {
					return result;
				}
			}

			return 0;
		};

		groups.sort(comparator);
	}




	/**
	 * Sorts the given {@link TaskGroup}s by the given {@link SortElement}s
	 */
	public static void sortTasks(List<Task> tasks, List<SortElement> dataSort) {

		for (int i = dataSort.size() - 1; i >= 0; i--) {
			SortElement sortElement = dataSort.get(i);

			final Comparator comparatorType = AttributeLogic.LOGIC_MODULES.get(sortElement.attribute.get().type.get())
					.getComparator(sortElement.dir.get());

			if (comparatorType != null) {
				Comparator<Task> comparatorTask = (tx, ty) -> {
					final TaskValue<?> vx = TaskLogic.getValueOrDefault(tx, sortElement.attribute.get());
					final TaskValue<?> vy = TaskLogic.getValueOrDefault(ty, sortElement.attribute.get());

					if (vx.getAttType() == null) {
						return (vy.getAttType() == null) ? 0 : -1;
					}
					if (vy.getAttType() == null) {
						return (vx.getAttType() == null) ? 0 : +1;
					}
					return comparatorType.compare(vx.getValue(), vy.getValue());
				};
				tasks.sort(comparatorTask);
			}

		}

	}




	/**
	 * @return the total amount of visible tasks of the given {@link Project}. Uses the last created groups.
	 */
	public static int countDisplayedTasks(Project project) {
		List<TaskGroup> groups = project.temporaryData.lastTaskGroups;
		int count = 0;
		for (TaskGroup group : groups) {
			count += group.tasks.size();
		}
		return count;
	}




	/**
	 * @return a title for the given {@link TaskGroup} (using the given sample {@link Task} as reference).
	 */
	public static String createTaskGroupTitle(Project project, TaskGroup group, Task sampleTask) {
		if (project.data.groupData.get() == null) {
			return "All Tasks";
		}

		String headerString = project.data.groupData.get().customHeaderString;
		String strHeader = "?";

		if (headerString == null) {
			StringBuilder strHeaderBuilder = new StringBuilder();
			for (TaskAttribute attribute : group.attributes) {
				TaskValue<?> taskValue = TaskLogic.getValueOrDefault(sampleTask, attribute);
				strHeaderBuilder.append(taskValue.asDisplayableString()).append(", ");
			}
			if (strHeaderBuilder.length() >= 2) {
				strHeader = strHeaderBuilder.toString().substring(0, strHeaderBuilder.length() - 2);
			}


		} else {
			strHeader = headerString;
			for (TaskAttribute attribute : group.attributes) {
				if (sampleTask == null) {
					strHeader = strHeader.replaceAll("\\{" + attribute.name.get() + "\\}", "?");
				} else {
					TaskValue<?> taskValue = TaskLogic.getValueOrDefault(sampleTask, attribute);
					strHeader = strHeader.replaceAll("\\{" + attribute.name.get() + "\\}", taskValue.asDisplayableString());
				}
			}
		}

		return strHeader;
	}




	/**
	 * notifies all registered listeners of changes.
	 */
	private static void callListenersDisplayChanged(Project project) {
		List<EventHandler<ActionEvent>> listeners = project.temporaryData.listenersTaskGroupsChanged;
		for (EventHandler<ActionEvent> listener : listeners) {
			listener.handle(new ActionEvent());
		}
	}




	static class FilterNode {


		public FilterCriteria criteria;
		public List<FilterNode> children = new ArrayList<>();




		public FilterNode(FilterCriteria criteria) {
			this.criteria = criteria;
		}




		public FilterNode(FilterCriteria criteria, List<FilterNode> children) {
			this.criteria = criteria;
			this.children.addAll(children);
		}




		public FilterNode(FilterCriteria criteria, FilterNode... children) {
			this.criteria = criteria;
			this.children.addAll(Arrays.asList(children));
		}




		/**
		 * Expands this node into a tree (recursive)
		 * */
		public void expand() {
			if (this.criteria.type == FilterCriteria.CriteriaType.OR) {
				for (FilterCriteria crit : ((OrFilterCriteria) this.criteria).subCriteria) {
					FilterNode node = new FilterNode(crit);
					node.expand();
					this.children.add(node);
				}
			}
			if (this.criteria.type == FilterCriteria.CriteriaType.AND) {
				for (FilterCriteria crit : ((AndFilterCriteria) this.criteria).subCriteria) {
					FilterNode node = new FilterNode(crit);
					node.expand();
					this.children.add(node);
				}
			}
		}



		/**
		 * @return true, if this node and its children match the given {@link Task}.
		 * */
		public boolean matches(Task task) {
			if (this.criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
				TerminalFilterCriteria terminal = (TerminalFilterCriteria) criteria;
				if (AttributeLogic.isValidFilterOperation(terminal)) {
					return AttributeLogic.LOGIC_MODULES.get(terminal.attribute.type.get()).matchesFilter(task, terminal);
				} else {
					return false;
				}
			}
			if (this.criteria.type == FilterCriteria.CriteriaType.OR) {
				for (FilterNode node : this.children) {
					if (node.matches(task)) {
						return true;
					}
				}
			}
			if (this.criteria.type == FilterCriteria.CriteriaType.AND) {
				for (FilterNode node : this.children) {
					if (!node.matches(task)) {
						return false;
					}
				}
				return true;
			}
			return false;
		}

	}

}