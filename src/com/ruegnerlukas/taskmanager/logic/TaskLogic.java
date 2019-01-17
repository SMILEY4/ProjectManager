package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

import java.util.List;

public class TaskLogic {



	public TaskLogic() {

		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent)e;
				if(Logic.project.isProjectOpen()) {

					List<Task> taskList = Logic.project.getProject().tasks;
					for(Task task : taskList) {
						removeAttribute(task, event.getAttribute());
					}

				}
			}
		}, AttributeRemovedEvent.class);

		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeCreatedEvent event = (AttributeCreatedEvent)e;
				if(Logic.project.isProjectOpen()) {

					List<Task> taskList = Logic.project.getProject().tasks;
					for(Task task : taskList) {
						setAttributeValue(task, event.getAttribute(), new NoValue());
					}

				}
			}
		}, AttributeCreatedEvent.class);

	}






	public boolean createTask() {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			Task task = new Task();
			for(TaskAttribute attribute : project.attributes) {
				task.attributes.put(attribute, new NoValue());
			}
			project.tasks.add(task);

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


}
