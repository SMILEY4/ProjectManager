package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.*;
import sun.rmi.runtime.Log;

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
				TaskFlag currFlag = ((FlagValue) getAttributeValue(task, FlagAttributeData.NAME)).getFlag();
				if (!flagData.hasFlag(currFlag)) {
					setAttributeValue(task, attribute, new FlagValue(flagData.defaultFlag));
				}
			}
		}

	}




	//======================//
	//        GETTER        //
	//======================//




	//======================//
	//        SETTER        //
	//======================//




	public void createNewTask() {

		Project project = Logic.project.getProject();
		if(project != null) {

			// init task
			Task task = new Task();
			// todo set attribs to NoValue


		}


		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			Task task = new Task();
			for (TaskAttribute attribute : project.attributes) {
				task.attributes.put(attribute, new NoValue());
			}
			project.tasks.add(task);

			// set fixed values (flag has default)
			setAttributeValue(task, Logic.attribute.getAttributes(TaskAttributeType.ID).get(0), new NumberValue(project.idCounter));
			setAttributeValue(task, Logic.attribute.getAttributes(TaskAttributeType.DESCRIPTION).get(0), new TextValue(""));

			project.idCounter++;

			EventManager.fireEvent(new TaskCreatedEvent(task, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean setAttributeValue(Task task, TaskAttribute attribute, TaskAttributeValue value) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			if (project.tasks.contains(task) && project.attributes.contains(attribute)) {

				TaskAttributeValue oldValue = task.attributes.containsKey(attribute) ? task.attributes.get(attribute) : new NoValue();

				boolean valid = attribute.data.validate(value);
				if (valid) {
					task.attributes.put(attribute, value);
					EventManager.fireEvent(new TaskValueChangedEvent(task, attribute, oldValue, value, this));
				} else {
					EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, oldValue, value, EventCause.NOT_ALLOWED, this));
				}
				return valid;

			} else {
				return false;
			}

		} else {
			return false;
		}
	}




	public boolean removeAttribute(Task task, TaskAttribute attribute) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			if (project.tasks.contains(task)) {
				return task.attributes.remove(attribute) != null;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}




	public TaskAttributeValue getAttributeValue(Task task, String attributeName) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			if (project.tasks.contains(task)) {

				TaskAttribute attribute = null;
				for (TaskAttribute attrib : task.attributes.keySet()) {
					if (attrib.name.equals(attributeName)) {
						attribute = attrib;
					}
				}

				if (attribute != null) {

					// value is set
					if (task.attributes.containsKey(attribute) && !(task.attributes.get(attribute) instanceof NoValue)) {
						return task.attributes.get(attribute);

						// use default values
					} else if (attribute.data.usesDefault()) {
						return attribute.data.getDefault();

						// no value found
					} else {
						return new NoValue();
					}
				}

				return null;

			} else {
				return null;
			}
		} else {
			return null;
		}
	}


}
