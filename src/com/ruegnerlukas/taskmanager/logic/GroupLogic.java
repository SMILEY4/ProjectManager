package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupHeaderChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupHeaderStringChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupOrderChangedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	}




	private void onAttributeDeleted(TaskAttribute attribute) {
		removeGroupElement(attribute);
	}




	protected boolean isAttributeRelevant(TaskAttribute attribute) {
		return Logic.project.getProject().taskGroupOrder.contains(attribute);
	}




	protected TaskGroupData applyGroups(List<Task> tasksInput) {

		Project project = Logic.project.getProject();

		// create new group data
		TaskGroupData taskGroupData = new TaskGroupData();
		taskGroupData.attributes.addAll(project.taskGroupOrder);

		// sort tasks into groups
		if (project.taskGroupOrder.isEmpty()) {
			TaskGroup group = new TaskGroup();
			group.tasks.addAll(tasksInput);
			taskGroupData.groups.add(group);

		} else {

			// build tree (first element is root)
			List<Node> tree = buildTree(tasksInput, project.taskGroupOrder);
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

		return taskGroupData;
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




	public void getCustomHeaderString(Request<String> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.useCustomHeaderString) {
				request.respond(new Response<>(Response.State.SUCCESS, "", project.taskGroupHeaderString));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "TaskGroups do not currently use a custom headerString"));
			}
		}
	}




	public void getTaskGroupOrder(Request<List<TaskAttribute>> request) {
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
	 * - GroupOrderChangedEvent: when the attributes where changed
	 */
	public void setGroupOrder(List<TaskAttribute> attribOrder) {
		Project project = Logic.project.getProject();
		if (project != null) {
			project.taskGroupOrder.clear();
			project.taskGroupOrder.addAll(attribOrder);
			EventManager.fireEvent(new GroupOrderChangedEvent(project.taskGroupOrder, this));
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
	 * - GroupOrderChangedEvent: when the list of TaskAttributes has been changed
	 */
	public void removeGroupElement(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.taskGroupOrder.contains(attribute)) {
				project.taskGroupOrder.remove(attribute);
				EventManager.fireEvent(new GroupOrderChangedEvent(project.taskGroupOrder, this));
			}
		}
	}


}
