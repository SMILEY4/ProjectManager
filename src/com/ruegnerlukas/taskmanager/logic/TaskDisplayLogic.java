package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.*;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TaskDisplayLogic {


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
//		for (Task task : project.data.tasks) {
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
//		for (Task task : project.data.tasks) {
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
//		sortTasks(project.data.tasks, dataSort);
//
//		System.out.println("=== SORTED ===");
//
//		for (Task task : project.data.tasks) {
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
//		List<TaskGroup> groups = createTaskGroups(project.data.tasks, new ArrayList<>(), dataGroup, new ArrayList<>());
//
//
//		System.out.println("=== ALL ===");
//
//		for (Task task : project.data.tasks) {
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
//			for (Task task : group.tasks) {
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





	public static List<TaskGroup> createTaskGroups(List<Task> tasks, List<FilterElement> dataFilter, List<TaskAttribute> dataGroup, List<SortElement> dataSort) {


		// 1. filter
		List<Task> filteredTasks = new ArrayList<>();
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			if (passesFilter(task, dataFilter)) {
				filteredTasks.add(task);
			}
		}


		// 2. group
		List<TaskGroup> groups = new ArrayList<>();

		TaskGroup root = new TaskGroup();
		root.tasks.addAll(filteredTasks);
		groups.add(root);

		for (TaskAttribute attribute : dataGroup) {
			List<TaskGroup> newGroups = new ArrayList<>();
			for (TaskGroup group : groups) {
				newGroups.addAll(splitTaskGroup(group, attribute));
			}
			groups.clear();
			groups.addAll(newGroups);
		}


		// 3. sort
		for (TaskGroup group : groups) {
			sortTasks(group.tasks, dataSort);
		}

		return groups;
	}




	public static boolean passesFilter(Task task, List<FilterElement> dataFilter) {
		// TODO
		return true;
	}




	private static List<TaskGroup> splitTaskGroup(TaskGroup group, TaskAttribute attribute) {

		// sort tasks into buckets
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

		for(int i=dataSort.size()-1; i>=0; i--) {
			SortElement sortElement = dataSort.get(i);

			final Comparator comparatorType = getComparator(sortElement.attribute.type.get());
			if(comparatorType != null) {
				Comparator<Task> comparatorTask = (tx, ty) -> {
					final Object vx = TaskLogic.getValue(tx, sortElement.attribute);
					final Object vy = TaskLogic.getValue(ty, sortElement.attribute);

					if(vx instanceof NoValue || vx == null) {
						return (vy instanceof NoValue || vy == null) ? 0 : -1;
					}
					if(vy instanceof NoValue || vy == null) {
						return (vx instanceof NoValue || vx == null) ? 0 : +1;
					}
					return comparatorType.compare(vx, vy);
				};
				tasks.sort(comparatorTask);
			}

		}

	}




	private static Comparator getComparator(AttributeType type) {
		try {
			Method method = AttributeLogic.LOGIC_CLASSED.get(type).getMethod("getComparator");
			return (Comparator) method.invoke(null);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}


}
