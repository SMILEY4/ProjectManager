package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	TaskLogic() {

		// attribute removed
		EventManager.registerListener(e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			onAttributeRemoved(event.getAttribute());
		}, AttributeRemovedEvent.class);


		// attribute created
		EventManager.registerListener(e -> {
			AttributeCreatedEvent event = (AttributeCreatedEvent) e;
			onAttributeCreated(event.getAttribute());
		}, AttributeCreatedEvent.class);


		// attribute changed
		EventManager.registerListener(e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			onAttributeChanged(event.getAttribute(), event.getChangedVars());
		}, AttributeUpdatedEvent.class);

		// recommend task-refresh
		EventManager.registerListener(e -> {
					EventManager.fireEvent(new RefreshTaskDisplayRecommendationEvent(this));
				},
				AttributeCreatedEvent.class,
				AttributeRemovedEvent.class,
				AttributeTypeChangedEvent.class,
				AttributeUpdatedEvent.class,
				TaskCreatedEvent.class,
				//TaskRemovedEvent.class, //TODO
				TaskValueChangedEvent.class,
				FilterCriteriaChangedEvent.class,
				GroupOrderChangedEvent.class,
				SortElementsChangedEvent.class
		);

	}




	protected List<Task> getTasksInternal() {
		return Logic.project.getProject().tasks;
	}




	private void onAttributeCreated(TaskAttribute attribute) {
		List<Task> tasks = getTasksInternal();
		for (int i = 0; i < tasks.size(); i++) {
			setAttributeValue(tasks.get(i), attribute, new NoValue());
		}
	}




	private void onAttributeRemoved(TaskAttribute attribute) {
		List<Task> tasks = getTasksInternal();
		for (int i = 0; i < tasks.size(); i++) {
			removeAttribute(tasks.get(i), attribute);
		}
	}




	private void onAttributeChanged(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars) {

		// update tasks when a TaskFlag was removed
		if (changedVars.containsKey(TaskAttributeData.Var.FLAG_ATT_FLAGS)) {
			FlagAttributeData flagData = (FlagAttributeData) Logic.attribute.findAttribute(TaskAttributeType.FLAG).data;
			List<Task> taskList = Logic.project.getProject().tasks;
			for (Task task : taskList) {
				TaskFlag currFlag = ((FlagValue) getValue(task, Logic.attribute.findAttribute(FlagAttributeData.NAME))).getFlag();
				if (!flagData.hasFlag(currFlag)) {
					setAttributeValue(task, attribute, new FlagValue(flagData.defaultFlag));
				}
			}
		}

	}




	protected boolean setValue(Task task, TaskAttribute attribute, TaskAttributeValue value) {
		if (attribute.data.validate(value)) {
			task.attributes.put(attribute, value);
			return true;
		} else {
			return false;
		}
	}




	protected TaskAttributeValue getValue(Task task, TaskAttribute attribute) {
		if (task.attributes.containsKey(attribute)) {
			return task.attributes.get(attribute);
		} else if (attribute.data.usesDefault()) {
			return attribute.data.getDefault();
		} else {
			return new NoValue();
		}
	}


	//======================//
	//        GETTER        //
	//======================//




	public void getTaskGroups(Request<TaskGroupData> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<Task> allTasks = project.tasks;
			List<Task> filteredTasks = Logic.filter.applyFilters(allTasks);
			TaskGroupData groupedTasks = Logic.group.applyGroups(filteredTasks);
			Logic.sort.applySort(groupedTasks);
			groupedTasks.tasks.addAll(filteredTasks);
			request.respond(new Response<>(Response.State.SUCCESS, groupedTasks));
		}
	}




	/**
	 * Requests a list of all tasks
	 */
	public void getTasks(Request<List<Task>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			request.respond(new Response<>(Response.State.SUCCESS, project.tasks));
		}
	}




	/**
	 * Requests a list of all tasks with the given value for a given attribute
	 */
	public void getTasks(TaskAttribute attribute, TaskAttributeValue value, Request<List<Task>> request) {
		Project project = Logic.project.getProject();
		if (project != null) {

			if (attribute.data.validate(value)) {
				List<Task> tasks = new ArrayList<>();
				for (int i = 0; i < project.tasks.size(); i++) {
					Task task = project.tasks.get(i);
					TaskAttributeValue taskValue = getValue(task, attribute);
					if (taskValue.equals(value)) {
						tasks.add(task);
					}
				}
				request.respond(new Response<>(Response.State.SUCCESS, tasks));

			} else {
				request.respond(new Response<List<Task>>(Response.State.FAIL, "Value '" + value + "' is invalid for '" + attribute + "'"));
			}

			request.respond(new Response<>(Response.State.SUCCESS, project.tasks));
		}
	}




	/**
	 * Request the value of the given task for the given attribute. If the value is not set for the task,
	 * it returns the default value or "NoValue"
	 */
	public void getAttributeValue(Task task, String attributeName, Request<TaskAttributeValue> request) {

		Project project = Logic.project.getProject();
		if (project != null) {

			if (task == null) {
				request.respond(new Response<>(Response.State.FAIL, "Task is null."));

			} else if (!project.tasks.contains(task)) {
				request.respond(new Response<>(Response.State.FAIL, "Task not part of project"));

			} else {

				TaskAttribute attribute = Logic.attribute.findAttribute(attributeName);
				if (attribute == null) {
					request.respond(new Response<>(Response.State.FAIL, "Attribute with name '" + attributeName + "' not found."));

				} else {
					TaskAttributeValue value = getValue(task, attribute);
					request.respond(new Response<>(Response.State.SUCCESS, value));

				}

			}

		}

	}


	//======================//
	//        SETTER        //
	//======================//




	/**
	 * Creates a new Task <p>
	 * Events: <p>
	 * - TaskCreatedEvent: when the task was created
	 */
	public void createNewTask() {

		Project project = Logic.project.getProject();
		if (project != null) {

			// create task
			Task task = new Task();

			// set default attrib-values
			for (TaskAttribute attribute : project.attributes) {
				if (attribute.data.usesDefault()) {
					setValue(task, attribute, attribute.data.getDefault());
				}
			}
			setValue(task, Logic.attribute.findAttribute(TaskAttributeType.ID), new NumberValue(project.idCounter++));

			// add to project
			project.tasks.add(task);
			EventManager.fireEvent(new TaskCreatedEvent(task, this));

		}

	}




	/**
	 * Sets the value of a given task and attribute to the given value <p>
	 * Events: <p>
	 * - TaskValueChangedRejection: when the value could not be changed (NOT_ALLOWED = the value is invalid , NOT_EXISTS = given task, attribute or value is null or is not part of project) <p>
	 * - TaskValueChangedRejection: when the value of the task and attribute was changed
	 */
	public void setAttributeValue(Task task, String attributeName, TaskAttributeValue value) {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute attribute = Logic.attribute.findAttribute(attributeName);
			setAttributeValue(task, attribute, value);
		}
	}




	/**
	 * Sets the value of a given task and attribute to the given value <p>
	 * Events: <p>
	 * - TaskValueChangedRejection: when the value could not be changed (NOT_ALLOWED = the value is invalid , NOT_EXISTS = given task, attribute or value is null or is not part of project) <p>
	 * - TaskValueChangedRejection: when the value of the task and attribute was changed
	 */
	public void setAttributeValue(Task task, TaskAttribute attribute, TaskAttributeValue value) {

		Project project = Logic.project.getProject();
		if (project != null) {

			if (task == null || attribute == null || value == null || !project.tasks.contains(task) || !project.attributes.contains(attribute)) {
				EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, null, value, EventCause.NOT_EXISTS, this));

			} else if (attribute.data.getType() == TaskAttributeType.ID) {
				EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, null, value, EventCause.NOT_ALLOWED, this));

			} else {
				TaskAttributeValue oldValue = getValue(task, attribute);
				if (!oldValue.equals(value)) {
					if (setValue(task, attribute, value)) {
						EventManager.fireEvent(new TaskValueChangedEvent(task, attribute, oldValue, value, this));
					} else {
						EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, oldValue, value, EventCause.NOT_ALLOWED, this));
					}
				}


			}

		}

	}




	/**
	 * Clears the value of a given task and attribute <p>
	 * Events: <p>
	 * - TaskValueChangedRejection: when the value could not be cleared (NOT_ALLOWED = the value is invalid, NOT_EXISTS = given task or attribute is null or is not part of project) <p>
	 * - TaskValueChangedRejection: when the value of the task and attribute was changed
	 */
	public void removeAttribute(Task task, TaskAttribute attribute) {

		Project project = Logic.project.getProject();
		if (project != null) {

			TaskAttributeValue oldValue = getValue(task, attribute);

			if (project.tasks.contains(task)) {

				if (attribute.data.getType() == TaskAttributeType.ID) {
					EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, null, null, EventCause.NOT_ALLOWED, this));
				} else {
					task.attributes.remove(attribute);
					EventManager.fireEvent(new TaskValueChangedEvent(task, attribute, oldValue, null, this));
				}

			} else {
				EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, oldValue, null, EventCause.NOT_EXISTS, this));

			}

		}

	}


}
