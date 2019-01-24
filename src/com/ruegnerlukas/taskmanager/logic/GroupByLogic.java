package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByOrderChangedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

import java.util.*;

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




//	public static void main(String[] args) {
//
//
//		for(Task task : Logic.project.getProject().tasks) {
//			System.out.println("Task " + Integer.toHexString(task.hashCode()));
//			FlagValue flagValue = (FlagValue)Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME);
//			NumberValue idValue = (NumberValue) Logic.tasks.getAttributeValue(task, IDAttributeData.NAME);
//			System.out.println("  - id   = " + idValue.getInt());
//			System.out.println("  - flag = " + flagValue.getFlag().name);
//
//		}
//
//
//		List<TaskAttribute> groupByOrder = new ArrayList<>();
//		groupByOrder.add( Logic.attribute.getAttributes(TaskAttributeType.FLAG).get(0));
//		groupByOrder.add( Logic.attribute.getAttributes(TaskAttributeType.ID).get(0));
//		Logic.groupBy.setGroupByOrder(groupByOrder);
//
//
//		System.out.println(); System.out.println(); System.out.println(); System.out.println();
//
//
//		Map<List<TaskAttributeValue>, List<Task>> taskLists = Logic.groupBy.getTaskLists();
//
//		for(List<TaskAttributeValue> values : taskLists.keySet()) {
//			System.out.println("TaskList: " + values);
//			for(Task task : taskLists.get(values)) {
//				System.out.println("  - Task " + Integer.toHexString(task.hashCode()));
//				FlagValue flagValue = (FlagValue)Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME);
//				NumberValue idValue = (NumberValue) Logic.tasks.getAttributeValue(task, IDAttributeData.NAME);
//				System.out.println("      - id   = " + idValue.getInt());
//				System.out.println("      - flag = " + flagValue.getFlag().name);
//			}
//		}
//
//
//	}




	public Map<List<TaskAttributeValue>, List<Task>> getTaskLists() {

		if(Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			Map<List<TaskAttributeValue>, List<Task>> map = new HashMap<>();

			if(project.groupByOrder.isEmpty()) {
				map.put(new ArrayList<>(), new ArrayList<>(project.tasks));

			} else {
				List<Node> tree = buildTree(project.tasks, project.groupByOrder);
				Node root = tree.get(0);

				for(int i=1; i<tree.size(); i++) {
					Node node = tree.get(i);

					List<TaskAttributeValue> values = new ArrayList<>();

					Node current = node;
					while(current != null) {
						values.add(current.value);
						current = current.parent;
					}

					values.remove(null);
					Collections.reverse(values);
					map.put(values, node.tasks);
				}


			}

			return map;

		} else {
			return null;
		}
	}




	private void printTree(Node root, int level) {

		String indent = "";
		for(int i=0; i<level; i++) {
			indent += "   ";
		}

		System.out.println(indent + "Node:" + Integer.toHexString(root.hashCode()));

		if(root.children.isEmpty()) {
			for(Task task : root.tasks) {
				System.out.println(indent + "  - Task " + Integer.toHexString(task.hashCode()));
				FlagValue flagValue = (FlagValue)Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME);
				NumberValue idValue = (NumberValue) Logic.tasks.getAttributeValue(task, IDAttributeData.NAME);
				System.out.println(indent + "      - id   = " + idValue.getInt());
				System.out.println(indent + "      - flag = " + flagValue.getFlag().name);
			}

		} else {
			for(Node node : root.children) {
				printTree(node, level+1);
			}
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
