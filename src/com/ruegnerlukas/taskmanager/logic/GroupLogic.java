package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.*;

public class GroupLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	GroupLogic() {

		// attribute deleted
		EventManager.registerListener(e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			onAttributeDeleted(event.getAttribute());
		}, AttributeRemovedEvent.class);

		// any change to attribute-values / tasks
		EventManager.registerListener(this, e -> {
			onChange();
		}, TaskGroupOrderChangedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class, FilteredTasksChangedEvent.class);

	}




	private void onAttributeDeleted(TaskAttribute attribute) {
		removeGroupElement(attribute);
	}




	private void onChange() {
		rebuildTaskGroups();
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
			TaskAttributeValue value = Logic.tasks.getValue(task, attribute);

			if (!map.containsKey(value)) {
				map.put(value, new ArrayList<>());
			}
			map.get(value).add(task);

		}
		return map;
	}




	private class Node {


		public Node parent;
		public List<Node> children = new ArrayList<>();
		public TaskAttribute attribute;
		public TaskAttributeValue value;
		public List<Task> tasks = new ArrayList<>();

	}


	//======================//
	//        GETTER        //
	//======================//




	public void getTaskGroups(Request request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.taskGroupData));
		}
	}




	public void getCustomHeaderString(Request request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.useCustomHeaderString) {
				request.respond(new Response<>(Response.State.SUCCESS, project.taskGroupHeaderString));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "TaskGroups do not currently use a custom headerString"));
			}
		}
	}




	public void getTaskGroupOrder(Request request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.taskGroupOrder));
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	/**
	 * Set the TaskAttributes and their order to group tasks <p>
	 * Events <p>
	 * - TaskGroupOrderChangedEvent: when the attributes where changed
	 */
	public void setGroupOrder(List<TaskAttribute> attribOrder) {
		Project project = Logic.project.getProject();
		if (project != null) {
			project.taskGroupOrder.clear();
			project.taskGroupOrder.addAll(attribOrder);
			EventManager.fireEvent(new TaskGroupOrderChangedEvent(project.taskGroupOrder, this));
		}
	}




	/**
	 * Sets the title of TaskGroups. Use {att.name} to insert the value of the TaskGroup of the specified attribute.  <p>
	 * Events <p>
	 * - GroupHeaderChangedEvent: when the title-string was changed
	 */
	public void setGroupHeaderString(String string) {
		Project project = Logic.project.getProject();
		if (project != null) {
			String oldString = project.taskGroupHeaderString;
			project.taskGroupHeaderString = string;
			EventManager.fireEvent(new GroupHeaderChangedEvent(oldString, string, this));
		}
	}




	/**
	 * Whether or not the custom title of TaskGroups should be used <p>
	 * Events <p>
	 * - GroupHeaderStringChangedEvent
	 */
	public void setUseCustomHeaderString(boolean useCustomHeaderString) {
		Project project = Logic.project.getProject();
		if (project != null) {
			final boolean prev = project.useCustomHeaderString;
			if (prev != useCustomHeaderString) {
				project.useCustomHeaderString = useCustomHeaderString;
				EventManager.fireEvent(new GroupHeaderStringChangedEvent(prev, useCustomHeaderString, this));
			}
		}
	}




	/**
	 * Removes an attribute <p>
	 * Events <p>
	 * - TaskGroupOrderChangedEvent: when the list of TaskAttributes has been changed
	 */
	public void removeGroupElement(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.taskGroupOrder.contains(attribute)) {
				project.taskGroupOrder.remove(attribute);
				EventManager.fireEvent(new TaskGroupOrderChangedEvent(project.taskGroupOrder, this));
			}
		}
	}




	/**
	 * Groups the (filtered) Tasks by the specified TaskAttributes <p>
	 * Events <p>
	 * - GroupByRebuildEvent
	 */
	public void rebuildTaskGroups() {

		Project project = Logic.project.getProject();
		if (project != null) {

			// create new group data
			TaskGroupData taskGroupData = new TaskGroupData();
			taskGroupData.attributes.addAll(project.taskGroupOrder);

			// sort tasks into groups
			if (project.taskGroupOrder.isEmpty()) {
				// add all task to single group

				TaskGroup group = new TaskGroup();
				group.tasks.addAll(project.filteredTasks);

			} else {
				// split task into multiple groups

				// build tree (first element is root)
				List<Node> tree = buildTree(project.filteredTasks, project.taskGroupOrder);
				Node root = tree.get(0);

				for (int i = 1; i < tree.size(); i++) {
					Node node = tree.get(i);
					TaskGroup group = new TaskGroup();

					Node current = node;
					while (current != null) {
						if (current.attribute != null && current.value != null) {
							group.values.put(current.attribute, current.value);
						}
						current = current.parent;
					}

					group.tasks.addAll(node.tasks);
					taskGroupData.groups.add(group);
				}

			}

			// sort groups
			Comparator<TaskGroup> comp = (a, b) -> {
				for (int i = 0; i < taskGroupData.attributes.size(); i++) {
					TaskAttribute attrib = taskGroupData.attributes.get(i);
					TaskAttributeValue valueA = a.values.get(attrib);
					TaskAttributeValue valueB = b.values.get(attrib);
					int cmp = valueA.compareTo(valueB);
					if (cmp != 0) {
						return cmp;
					}
				}
				return 0;
			};
			taskGroupData.groups.sort(comp);

			// finish
			project.taskGroupData = taskGroupData;
			EventManager.fireEvent(new GroupByRebuildEvent(taskGroupData, this));

		}
	}


}
