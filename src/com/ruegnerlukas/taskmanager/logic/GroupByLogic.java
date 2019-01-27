package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.groups.GroupByData;
import com.ruegnerlukas.taskmanager.logic.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

import java.util.*;

public class GroupByLogic {


	protected GroupByLogic() {

		// attribute deleted -> remove from groupBy-values
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent) e;
				removeGroupByElement(event.getAttribute());
			}
		}, AttributeRemovedEvent.class);

		// any change to attributevalues / tasks -> rebuildTaskGroups TaskGroups
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				rebuildTaskGroups();
			}
		}, GroupByOrderChangedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class, FilteredTasksChangedEvent.class);

	}




	/**
	 * Set the TaskAttributes and their order to group tasks <p>
	 * Events <p>
	 * - GroupByOrderChangedEvent: when the attributes where changed
	 *
	 * @return true, if completed successful
	 */
	public boolean setGroupByOrder(List<TaskAttribute> attribOrder) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			project.groupByOrder.clear();
			project.groupByOrder.addAll(attribOrder);
			EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			return true;
		} else {
			return false;
		}
	}




	/**
	 * Sets the title of TaskGroups. Use {att.name} to insert the value of the TaskGroup of the specified attribute.  <p>
	 * Events <p>
	 * - GroupByHeaderChangedEvent: when the title-string was changed
	 *
	 * @return true, if completed successful
	 */
	public boolean setGroupHeaderString(String string) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			String oldString = project.groupByHeaderString;
			project.groupByHeaderString = string;
			EventManager.fireEvent(new GroupByHeaderChangedEvent(oldString, string, this));
			return true;
		} else {
			return false;
		}
	}




	/**
	 * Whether or not the custom title of TaskGroups should be used <p>
	 * Events <p>
	 * - GroupHeaderStringChangedEvent
	 *
	 * @return true, if completed successful
	 */
	public boolean setUseCustomHeaderString(boolean useCustomHeaderString) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			boolean prev = project.useCustomHeaderString;
			if(prev != useCustomHeaderString) {
				project.useCustomHeaderString = useCustomHeaderString;
				EventManager.fireEvent(new GroupHeaderStringChangedEvent(prev, useCustomHeaderString, this));
			}
			return true;
		} else {
			return false;
		}
	}




	/**
	 * Removes an attribute <p>
	 * Events <p>
	 * - GroupByOrderChangedEvent: when the list of TaskAttributes has been changed
	 *
	 * @return true, if completed successful
	 */
	public boolean removeGroupByElement(TaskAttribute attribute) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			if (project.groupByOrder.contains(attribute)) {
				project.groupByOrder.remove(attribute);
				EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			}
			return project.groupByOrder.remove(attribute);
		} else {
			return false;
		}
	}




	/**
	 * Groups the (filtered) Tasks by the specified TaskAttributes <p>
	 * Events <p>
	 * - GroupByRebuildEvent
	 *
	 * @return true, if completed successful
	 */
	public boolean rebuildTaskGroups() {

		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			GroupByData groupByData = new GroupByData();
			groupByData.attributes.addAll(project.groupByOrder);

			if (project.groupByOrder.isEmpty()) {
				TaskGroup group = new TaskGroup();
				group.tasks.addAll(project.filteredTasks);

			} else {

				List<Node> tree = buildTree(project.filteredTasks, project.groupByOrder);
				Node root = tree.get(0);
				for (int i = 1; i < tree.size(); i++) {
					Node node = tree.get(i);
					TaskGroup group = new TaskGroup();

					Node current = node;
					while (current != null) {
						if(current.attribute != null && current.value != null) {
							group.values.put(current.attribute, current.value);
						}
						current = current.parent;
					}

					group.tasks.addAll(node.tasks);
					groupByData.groups.add(group);
				}

			}

			Comparator<TaskGroup> comp = new Comparator<TaskGroup>() {
				@Override
				public int compare(TaskGroup a, TaskGroup b) {
					for(int i=0; i<groupByData.attributes.size(); i++) {
						TaskAttribute attrib = groupByData.attributes.get(i);
						TaskAttributeValue valueA = a.values.get(attrib);
						TaskAttributeValue valueB = b.values.get(attrib);
						int cmp = valueA.compareTo(valueB);
						if(cmp != 0) {
							return cmp;
						}
					}
					return 0;
				}
			};
			groupByData.groups.sort(comp);

			project.groupByData = groupByData;
			EventManager.fireEvent(new GroupByRebuildEvent(groupByData, this));
			return true;

		} else {
			return false;
		}
	}




	private List<Node> buildTree(List<Task> tasks, List<TaskAttribute> attributes) {

		List<Node> outList = new ArrayList<>();
		List<Node> workingNodes = new ArrayList<>();

		Node root = new Node();
		root.tasks.addAll(tasks);
		workingNodes.add(root);
		outList.add(root);


		for (int i = 0; i < attributes.size(); i++) {
			TaskAttribute attribute = attributes.get(i);
			List<Node> openedNodes = new ArrayList<>();

			for (Node node : workingNodes) {

				if (!node.tasks.isEmpty()) {
					Map<TaskAttributeValue, ArrayList<Task>> split = split(node.tasks, attribute);

					for (TaskAttributeValue value : split.keySet()) {
						Node newNode = new Node();
						newNode.parent = node;
						newNode.attribute = attribute;
						newNode.value = value;
						newNode.tasks = split.get(value);
						node.children.add(newNode);
						openedNodes.add(newNode);
					}

				}

			}

			workingNodes = openedNodes;

			if (i == attributes.size() - 1) {
				outList.addAll(workingNodes);
			}
		}

		return outList;
	}




	private Map<TaskAttributeValue, ArrayList<Task>> split(List<Task> tasks, TaskAttribute attribute) {
		Map<TaskAttributeValue, ArrayList<Task>> map = new HashMap<>();

		for (Task task : tasks) {
			TaskAttributeValue value = Logic.tasks.getAttributeValue(task, attribute.name);

			if (!map.containsKey(value)) {
				map.put(value, new ArrayList<>());
			}
			map.get(value).add(task);

		}
		return map;
	}




	class Node {
		public Node parent;
		public List<Node> children = new ArrayList<>();
		public TaskAttribute attribute;
		public TaskAttributeValue value;
		public List<Task> tasks = new ArrayList<>();
	}


}
