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

import java.util.List;

public class TaskLogic {



	TaskLogic() {

		// attribute removed
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				if(Logic.project.isProjectOpen()) {

					AttributeRemovedEvent event = (AttributeRemovedEvent)e;
					List<Task> taskList = Logic.project.getProject().tasks;
					for(Task task : taskList) {
						removeAttribute(task, event.getAttribute());
					}

				}
			}
		}, AttributeRemovedEvent.class);

		// attribute created
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				if(Logic.project.isProjectOpen()) {
					AttributeCreatedEvent event = (AttributeCreatedEvent)e;
					List<Task> taskList = Logic.project.getProject().tasks;
					for(Task task : taskList) {
						setAttributeValue(task, event.getAttribute(), new NoValue());
					}
				}
			}
		}, AttributeCreatedEvent.class);

		// attribute changed
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				if(Logic.project.isProjectOpen()) {
					AttributeUpdatedEvent event = (AttributeUpdatedEvent)e;

					// update tasks when TaskFlag was removed
					if(event.wasChanged(TaskAttributeData.Var.FLAG_ATT_FLAGS)) {
						FlagAttributeData flagData = (FlagAttributeData)Logic.attribute.getAttributes(TaskAttributeType.FLAG).get(0).data;
						List<Task> taskList = Logic.project.getProject().tasks;
						for(Task task : taskList) {
							TaskFlag currFlag = ((FlagValue)getAttributeValue(task, FlagAttributeData.NAME)).getFlag();
							if(!flagData.hasFlag(currFlag)) {
								setAttributeValue(task, event.getAttribute(), new FlagValue(flagData.defaultFlag));
							}
						}
					}

				}
			}
		}, AttributeUpdatedEvent.class);

	}






	public boolean createTask() {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			Task task = new Task();
			for(TaskAttribute attribute : project.attributes) {
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
			if(project.tasks.contains(task) && project.attributes.contains(attribute)) {

				TaskAttributeValue oldValue = task.attributes.containsKey(attribute) ? task.attributes.get(attribute) : new NoValue();

				boolean valid = attribute.data.validate(value);
				if(valid) {
					task.attributes.put(attribute, value);
					EventManager.fireEvent(new TaskValueChangedEvent(task, attribute, oldValue, value, this));
				} else {
					EventManager.fireEvent(new TaskValueChangedRejection(task, attribute, oldValue, value, EventCause.NOT_ALLOWED,this));
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
			if(project.tasks.contains(task)) {
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

			if(project.tasks.contains(task)) {

				TaskAttribute attribute = null;
				for(TaskAttribute attrib : task.attributes.keySet()) {
					if(attrib.name.equals(attributeName)) {
						attribute = attrib;
					}
				}

				if(attribute != null) {

					// value is set
					if(task.attributes.containsKey(attribute) && !(task.attributes.get(attribute) instanceof NoValue))  {
						return task.attributes.get(attribute);

					// use default values
					} else if(attribute.data.usesDefault()) {
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
