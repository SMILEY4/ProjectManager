package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogicManager;
import javafx.collections.ListChangeListener;

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

				// add listeners to filter/group/sort-data
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




	private static void onTaskAdded(Project project, Task task) {
		if (project.temporaryData.lastGroupsValid.get()) {
			// check if task passes filter
			if (project.data.filterData.get() != null) {
				FilterNode rootFilter = new FilterNode(project.data.filterData.get());
				rootFilter.expand();
				if (!rootFilter.matches(task)) {
					return;
				}
			}

			// find taskgroup
			TaskGroup taskGroup = null;
			if (project.data.groupData.get() == null && project.temporaryData.lastTaskGroups.size() == 1) {
				taskGroup = project.temporaryData.lastTaskGroups.get(0);

			} else {
				outer:
				for (TaskGroup group : project.temporaryData.lastTaskGroups) {
					Task taskRef = group.tasks.get(0);

					for (TaskAttribute attribute : group.attributes) {
						Object valueRef = TaskLogic.getValue(taskRef, attribute);
						Object valueTask = TaskLogic.getValue(task, attribute);
						if (!valueRef.equals(valueTask)) {
							continue outer;
						}
					}

					taskGroup = group;
					break;
				}
			}

			if (taskGroup == null) {
				taskGroup = new TaskGroup();
				taskGroup.tasks.add(task);
				taskGroup.attributes.setAll(project.data.groupData.get() == null ? new ArrayList<>() : project.data.groupData.get().attributes);
				project.temporaryData.lastTaskGroups.add(taskGroup);

			} else {
				taskGroup.tasks.add(task);
				if (project.data.sortData.get() != null) {
					sortTasks(taskGroup.tasks, project.data.sortData.get().sortElements);
				}
			}


		}

	}




	private static void onTaskRemoved(Project project, Task task) {
		if (project.temporaryData.lastGroupsValid.get()) {
			TaskGroup taskGroup = null;
			for (TaskGroup group : project.temporaryData.lastTaskGroups) {
				if (group.tasks.contains(task)) {
					taskGroup = group;
					break;
				}
			}
			if (taskGroup != null) {
				taskGroup.tasks.remove(task);
				if (taskGroup.tasks.isEmpty()) {
					project.temporaryData.lastTaskGroups.remove(taskGroup);
				}
			}
		}
	}




	public static void onTaskModified(Project project, Task task, TaskAttribute attribute) {
		if(project.data.tasks.contains(task)) {
			onTaskRemoved(project, task);
			onTaskAdded(project, task);
		}
	}




	public static List<TaskGroup> createTaskGroups(Project project) {
		return createTaskGroups(project, false);
	}




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
			return taskGroups;
		}
	}




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


		// 2. group
		List<TaskGroup> groups = new ArrayList<>();

		TaskGroup root = new TaskGroup();
		root.tasks.addAll(filteredTasks);
		groups.add(root);

		for (TaskAttribute attribute : (dataGroup == null ? new ArrayList<TaskAttribute>() : dataGroup.attributes)) {
			List<TaskGroup> newGroups = new ArrayList<>();
			for (TaskGroup group : groups) {
				newGroups.addAll(splitTaskGroup(group, attribute));
			}
			groups.clear();
			groups.addAll(newGroups);
		}


		// 3. sort
		if (dataSort != null) {
			for (TaskGroup group : groups) {
				sortTasks(group.tasks, dataSort.sortElements);
			}
		}

		return groups;
	}




	private static List<TaskGroup> splitTaskGroup(TaskGroup group, TaskAttribute attribute) {

		// sort projectdata into buckets
		Map<Object, List<Task>> buckets = new HashMap<>();
		for (int i = 0, n = group.tasks.size(); i < n; i++) {
			Task task = group.tasks.get(i);
			Object value = TaskLogic.getValue(task, attribute);

			if (!buckets.containsKey(value)) {
				buckets.put(value, new ArrayList<>());
			}
			buckets.get(value).add(task);
		}

		// convert buckets to groups
		List<TaskGroup> groups = new ArrayList<>();
		for (Object value : buckets.keySet()) {

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




	public static void sortTasks(List<Task> tasks, List<SortElement> dataSort) {

		for (int i = dataSort.size() - 1; i >= 0; i--) {
			SortElement sortElement = dataSort.get(i);

			final Comparator comparatorType = sortElement.dir.get() == SortElement.SortDir.ASC ?
					AttributeLogicManager.getComparatorAsc(sortElement.attribute.get().type.get())
					: AttributeLogicManager.getComparatorDesc(sortElement.attribute.get().type.get());

			if (comparatorType != null) {
				Comparator<Task> comparatorTask = (tx, ty) -> {
					final Object vx = TaskLogic.getValue(tx, sortElement.attribute.get());
					final Object vy = TaskLogic.getValue(ty, sortElement.attribute.get());

					if (vx instanceof NoValue || vx == null) {
						return (vy instanceof NoValue || vy == null) ? 0 : -1;
					}
					if (vy instanceof NoValue || vy == null) {
						return (vx instanceof NoValue || vx == null) ? 0 : +1;
					}
					return comparatorType.compare(vx, vy);
				};
				tasks.sort(comparatorTask);
			}

		}

	}


//	static Project project;
//
//
//
//
//	public static void main(String[] args) {

//		project = ProjectLogic.createNewProject();
//
//		ProjectLogic.addAttributeToProject(project, AttributeLogic.createTaskAttribute(AttributeType.BOOLEAN, "BoolAttribute"));
//		ProjectLogic.addAttributeToProject(project, AttributeLogic.createTaskAttribute(AttributeType.NUMBER, "NumberAttribute"));
//
//		for (int i = 0; i < 20; i++) {
//			ProjectLogic.addTaskToProject(project, TaskLogic.createTask(project));
//		}
//
//		Random random = new Random();
//		for (Task task : project.data.projectdata) {
//			TaskLogic.setValue(task, AttributeLogic.findAttribute(project, AttributeType.DESCRIPTION), "d" + random.nextInt(4));
//			TaskLogic.setValue(task, AttributeLogic.findAttribute(project, "BoolAttribute"), random.nextBoolean());
//			TaskLogic.setValue(task, AttributeLogic.findAttribute(project, "NumberAttribute"), (double)random.nextInt(20));
//		}

//		List<SortElement> dataSort = new ArrayList<>();
//		dataSort.add(new SortElement(AttributeLogic.findAttribute(project, "NumberAttribute"), SortElement.SortDir.ASC));
//		dataSort.add(new SortElement(AttributeLogic.findAttribute(project, AttributeType.DESCRIPTION), SortElement.SortDir.ASC));
//		dataSort.add(new SortElement(AttributeLogic.findAttribute(project, "BoolAttribute"), SortElement.SortDir.ASC));
//
//
//		System.out.println("=== ALL ===");
//
//		for (Task task : project.data.projectdata) {
//			String strTask = "   - ";
//			strTask += TaskLogic.getValue(task, AttributeLogic.findAttribute(project, AttributeType.ID)) + ":  ";
//			for (SortElement e : dataSort) {
//				strTask += TaskLogic.getValue(task, e.attribute) + ",  ";
//			}
//			System.out.println(strTask);
//		}
//
//		System.out.println();
//		System.out.println();
//
//		sortTasks(project.data.projectdata, dataSort);
//
//		System.out.println("=== SORTED ===");
//
//		for (Task task : project.data.projectdata) {
//			String strTask = "   - ";
//			strTask += TaskLogic.getValue(task, AttributeLogic.findAttribute(project, AttributeType.ID)) + ":  ";
//			for (SortElement e : dataSort) {
//				strTask += TaskLogic.getValue(task, e.attribute) + ",  ";
//			}
//			System.out.println(strTask);
//		}
//
//		System.out.println();
//		System.out.println();


//		List<TaskAttribute> dataGroup = new ArrayList<>();
//		dataGroup.add(AttributeLogic.findAttribute(project, AttributeType.DESCRIPTION));
//		dataGroup.add(AttributeLogic.findAttribute(project, "BoolAttribute"));
//
//		List<TaskGroup> groups = createTaskGroups(project.data.projectdata, new ArrayList<>(), dataGroup, new ArrayList<>());
//
//
//		System.out.println("=== ALL ===");
//
//		for (Task task : project.data.projectdata) {
//			String strTask = "   - ";
//			strTask += TaskLogic.getValue(task, AttributeLogic.findAttribute(project, AttributeType.ID)) + ":  ";
//			for (TaskAttribute attribute : dataGroup) {
//				strTask += TaskLogic.getValue(task, attribute) + ",  ";
//			}
//			System.out.println(strTask);
//		}
//
//		System.out.println();
//		System.out.println();
//
//
//		for (TaskGroup group : groups) {
//
//			System.out.println();
//			System.out.println();
//			System.out.println("=== GROUP ===");
//
//			for (TaskAttribute attribute : group.attributes) {
//				System.out.println("* " + attribute.name.get());
//			}
//
//			for (Task task : group.projectdata) {
//				String strTask = "   - ";
//				strTask += TaskLogic.getValue(task, AttributeLogic.findAttribute(project, AttributeType.ID)) + ":  ";
//				for (TaskAttribute attribute : group.attributes) {
//					strTask += TaskLogic.getValue(task, attribute) + ",  ";
//				}
//				System.out.println(strTask);
//			}
//
//
//		}

//
//	}


}
