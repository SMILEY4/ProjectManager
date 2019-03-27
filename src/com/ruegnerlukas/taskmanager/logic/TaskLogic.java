package com.ruegnerlukas.taskmanager.logic;

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
import com.ruegnerlukas.taskmanager.logic.attributes.validation.AttributeValidator;

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

		// recommend task-group-refresh
		EventManager.registerListener(e -> {
			EventManager.fireEvent(new RefreshTaskDisplayRecommendationEvent(this));
		}, AttributeRemovedEvent.class, FilterCriteriaChangedEvent.class, GroupOrderChangedEvent.class, SortElementsChangedEvent.class, TaskCreatedEvent.class, TaskDeletedEvent.class, PresetLoadEvent.class);

		EventManager.registerListener(e -> {
			AttributeTypeChangedEvent event = (AttributeTypeChangedEvent) e;
			if (isAttributeRelevant(event.getAttribute()) || Logic.filter.isAttributeRelevant(event.getAttribute()) || Logic.group.isAttributeRelevant(event.getAttribute()) || Logic.sort.isAttributeRelevant(event.getAttribute())) {
				EventManager.fireEvent(new RefreshTaskDisplayRecommendationEvent(this));
			}
		}, AttributeTypeChangedEvent.class);

		EventManager.registerListener(e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (isAttributeRelevant(event.getAttribute()) || Logic.filter.isAttributeRelevant(event.getAttribute()) || Logic.group.isAttributeRelevant(event.getAttribute()) || Logic.sort.isAttributeRelevant(event.getAttribute())) {
				EventManager.fireEvent(new RefreshTaskDisplayRecommendationEvent(this));
			}
		}, AttributeUpdatedEvent.class);

		EventManager.registerListener(e -> {
			TaskValueChangedEvent event = (TaskValueChangedEvent) e;
			if (isAttributeRelevant(event.getAttribute()) || Logic.filter.isAttributeRelevant(event.getAttribute()) || Logic.group.isAttributeRelevant(event.getAttribute()) || Logic.sort.isAttributeRelevant(event.getAttribute())) {
				EventManager.fireEvent(new RefreshTaskDisplayRecommendationEvent(this));
			}
		}, TaskValueChangedEvent.class);

	}




	private boolean isAttributeRelevant(TaskAttribute attribute) {
		TaskAttributeType type = attribute.data.getType();
		return (type == TaskAttributeType.FLAG || type == TaskAttributeType.DESCRIPTION || type == TaskAttributeType.ID);
	}




	protected List<Task> getTasksInternal() {
		return Logic.project.getProject().tasks;
	}




	private void onAttributeCreated(TaskAttribute attribute) {
//		List<Task> tasks = getTasksInternal();
//		for (int i = 0; i < tasks.size(); i++) {
//			setAttributeValue(tasks.get(i), attribute, new NoValue());
//		}
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
		AttributeValidator validator = AttributeLogic.VALIDATOR_MAP.get(attribute.data.getType());
		if (validator.validate(attribute.data, value)) {
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




	protected boolean hasValue(Task task, TaskAttribute attribute) {
		return task.attributes.containsKey(attribute);
	}


	//======================//
	//        GETTER        //
	//======================//




	public Response<List<Task>> getTaskWithValue(TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<Task> allTasks = project.tasks;
			List<Task> tasks = new ArrayList<>();
			for (int i = 0; i < allTasks.size(); i++) {
				Task task = allTasks.get(i);
				if (hasValue(task, attribute)) {
					tasks.add(task);
				}
			}
			return new Response<List<Task>>().complete(tasks);

		} else {
			return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	public Response<TaskGroupData> getTaskGroups() {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<Task> allTasks = project.tasks;
			List<Task> filteredTasks = Logic.filter.applyFilters(allTasks);
			TaskGroupData groupedTasks = Logic.group.applyGroups(filteredTasks);
			Logic.sort.applySort(groupedTasks);
			groupedTasks.tasks.addAll(filteredTasks);
			return new Response<TaskGroupData>().complete(groupedTasks);
		} else {
			return new Response<TaskGroupData>().complete(null, Response.State.FAIL);
		}
	}




	/**
	 * Requests a list of all tasks
	 */
	public Response<List<Task>> getTasks() {
		Project project = Logic.project.getProject();
		if (project != null) {
			return new Response<List<Task>>().complete(project.tasks);
		} else {
			return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	/**
	 * Requests a list of all tasks with the given value for a given attribute
	 */
	public Response<List<Task>> getTasks(TaskAttribute attribute, TaskAttributeValue value) {
		Project project = Logic.project.getProject();
		if (project != null) {

			AttributeValidator validator = AttributeLogic.VALIDATOR_MAP.get(attribute.data.getType());
			if (validator.validate(attribute.data, value)) {
				List<Task> tasks = new ArrayList<>();
				for (int i = 0; i < project.tasks.size(); i++) {
					Task task = project.tasks.get(i);
					TaskAttributeValue taskValue = getValue(task, attribute);
					if (taskValue.equals(value)) {
						tasks.add(task);
					}
				}

			} else {
				return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
			}

			return new Response<List<Task>>().complete(project.tasks);


		} else {
			return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	/**
	 * Request the value of the given task for the given attribute. If the value is not set for the task,
	 * it returns the default value or "NoValue"
	 */
	public Response<TaskAttributeValue> getAttributeValue(Task task, String attributeName) {

		Project project = Logic.project.getProject();
		if (project != null) {

			if (task == null) {
				return new Response<TaskAttributeValue>().complete(null, Response.State.FAIL);

			} else if (!project.tasks.contains(task)) {
				return new Response<TaskAttributeValue>().complete(null, Response.State.FAIL);

			} else {

				TaskAttribute attribute = Logic.attribute.findAttribute(attributeName);
				if (attribute == null) {
					return new Response<TaskAttributeValue>().complete(null, Response.State.FAIL);

				} else {
					TaskAttributeValue value = getValue(task, attribute);
					return new Response<TaskAttributeValue>().complete(value);
				}

			}

		} else {
			return new Response<TaskAttributeValue>().complete(null, Response.State.FAIL);
		}

	}




	public Response<Task> getTaskByID(int id) {
		Project project = Logic.project.getProject();
		if (project != null) {
			for (int i = 0, n = getTasksInternal().size(); i < n; i++) {
				Task task = getTasksInternal().get(i);
				if (task.getID() == id) {
					return new Response<Task>().complete(task);
				}
			}
			return new Response<Task>().complete(null, Response.State.FAIL);
		} else {
			return new Response<Task>().complete(null, Response.State.FAIL);
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
	public Response<Task> createNewTask() {

		Project project = Logic.project.getProject();
		if (project != null) {

			// createItem task
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
			return new Response<Task>().complete(task);

		} else {
			return new Response<Task>().complete(null, Response.State.FAIL);
		}
	}




	/**
	 * Deletes the given task. The task is still saved in the archived-task-list <p>
	 * Events: <p>
	 * - TaskDeletedEvent: when the task was deleted
	 */
	public void deleteTask(Task task) {
		Project project = Logic.project.getProject();
		if (project != null) {
			boolean removed = project.tasks.remove(task);
			if (removed) {

				project.archivedTasks.add(task);
				if (project.archivedTasks.size() > project.archivedTasksLimit) {
					project.archivedTasks.remove(0);
				}

				List<TaskAttribute> attributes = Logic.attribute.findAttributes(TaskAttributeType.DEPENDENCY);
				for (TaskAttribute attribute : attributes) {
					List<Task> dependOn = Logic.dependencies.getDependentOnInternal(task, attribute);
					for (Task dep : dependOn) {
						Logic.dependencies.deleteDependency(dep, task, attribute);
					}
				}

				EventManager.fireEvent(new TaskDeletedEvent(task, this));
			}
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




	public static final String CORR_BEH_DELETE = "Delete values";
	public static final String CORR_BEH_DEFAULT = "Set Values to default value";




	public void correctTaskValues(TaskAttribute attribute, String behaviour, boolean onlyInvalid) {

		Project project = Logic.project.getProject();
		if (project != null) {

			List<Task> allTasks = project.tasks;
			List<Task> affectedTasks = new ArrayList<>();
			for (int i = 0; i < allTasks.size(); i++) {
				Task task = allTasks.get(i);
				if (hasValue(task, attribute)) {
					affectedTasks.add(task);
				}
			}

			for (Task task : affectedTasks) {

				TaskAttributeValue value = getValue(task, attribute);
				AttributeValidator validator = AttributeLogic.VALIDATOR_MAP.get(attribute.data.getType());
				if (onlyInvalid && validator.validate(attribute.data, value)) {
					continue;
				}

				if (behaviour.equals(CORR_BEH_DELETE)) {
					removeAttribute(task, attribute);
				}
				if (behaviour.equals(CORR_BEH_DEFAULT)) {
					if (attribute.data.usesDefault()) {
						setAttributeValue(task, attribute, attribute.data.getDefault());
					} else {
						removeAttribute(task, attribute);
					}
				}

			}

		}

	}


}
