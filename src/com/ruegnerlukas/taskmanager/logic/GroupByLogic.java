package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByHeaderChangedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByOrderChangedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.groups.GroupByData;
import com.ruegnerlukas.taskmanager.logic.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupByLogic {



	protected GroupByLogic() {

		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent)e;
				removeGroupByElement(event.getAttribute());
			}
		}, AttributeRemovedEvent.class);

	}


	public boolean setGroupByOrder(List<TaskAttribute> attribOrder) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			project.groupByOrder.clear();
			project.groupByOrder.addAll(attribOrder);
			EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean setGroupHeaderString(String string) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			String oldString = project.groupByHeaderString;
			project.groupByHeaderString = string;
			EventManager.fireEvent(new GroupByHeaderChangedEvent(oldString, string, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean removeGroupByElement(TaskAttribute attribute) {
		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			if(project.groupByOrder.contains(attribute)) {
				project.groupByOrder.remove(attribute);
				EventManager.fireEvent(new GroupByOrderChangedEvent(project.groupByOrder, this));
			}
			return project.groupByOrder.remove(attribute);
		} else {
			return false;
		}
	}




	public boolean updateTaskGroups() {

		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			GroupByData groupByData = new GroupByData();
			groupByData.attributes.addAll(project.groupByOrder);

			if(project.groupByOrder.isEmpty()) {
				TaskGroup group = new TaskGroup();
				group.tasks.addAll(project.tasks);

			} else {

				List<Node> tree = buildTree(project.tasks, project.groupByOrder);
				for(int i=0; i<tree.size(); i++) {
					Node node = tree.get(0);
					TaskGroup group = new TaskGroup();

					Node current = node;
					while(current != null) {
						group.attributes.put(node.attribute, node.value);
						current = current.parent;
					}

					group.tasks.addAll(node.tasks);
					groupByData.groups.add(group);
				}

			}

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


		for(int i=0; i<attributes.size(); i++) {
			TaskAttribute attribute = attributes.get(i);
			List<Node> openedNodes = new ArrayList<>();

			for(Node node : workingNodes) {

				if(!node.tasks.isEmpty()) {
					Map<TaskAttributeValue, ArrayList<Task>> split = split(node.tasks, attribute);

					for(TaskAttributeValue value : split.keySet()) {
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

			if(i == attributes.size()-1) {
				outList.addAll(workingNodes);
			}
		}

		return outList;
	}




	private Map<TaskAttributeValue, ArrayList<Task>> split(List<Task> tasks, TaskAttribute attribute) {
		Map<TaskAttributeValue, ArrayList<Task>> map = new HashMap<>();

		for(Task task : tasks) {
			TaskAttributeValue value = Logic.tasks.getAttributeValue(task, attribute.name);

			if(!map.containsKey(value)) {
				map.put(value, new ArrayList<>());
			}
			map.get(value).add(task);

		}
		return map;
	}




	class Node {
		public Node 				parent;
		public List<Node> 			children = new ArrayList<>();
		public TaskAttribute 		attribute;
		public TaskAttributeValue 	value;
		public List<Task> 			tasks = new ArrayList<>();
	}


}
