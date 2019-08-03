package com.ruegnerlukas.taskmanager.logic.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class TaskValueChangeEvent extends Event {


	public static final EventType<TaskValueChangeEvent> TASKVALUE_CHANGED =
			new EventType<>(Event.ANY, "TASKVALUE_CHANGED");


	private Task task;
	private TaskAttribute attribute;
	private TaskValue<?> prevValue;
	private TaskValue<?> newValue;




	public TaskValueChangeEvent(Task task, TaskAttribute attribute, TaskValue<?> prevValue, TaskValue<?> newValue) {
		super(TASKVALUE_CHANGED);
		this.task = task;
		this.attribute = attribute;
		this.prevValue = prevValue;
		this.newValue = newValue;
	}




	public TaskValueChangeEvent(Object source, EventTarget target, Task task, TaskAttribute attribute, TaskValue<?> prevValue, TaskValue<?> newValue) {
		super(source, target, TASKVALUE_CHANGED);
		this.task = task;
		this.attribute = attribute;
		this.prevValue = prevValue;
		this.newValue = newValue;
	}




	@Override
	public TaskValueChangeEvent copyFor(Object newSource, EventTarget newTarget) {
		return (TaskValueChangeEvent) super.copyFor(newSource, newTarget);
	}




	@Override
	public EventType<? extends TaskValueChangeEvent> getEventType() {
		return (EventType<? extends TaskValueChangeEvent>) super.getEventType();
	}




	public Task getTask() {
		return task;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskValue<?> getPrevValue() {
		return prevValue;
	}




	public TaskValue<?> getNewValue() {
		return newValue;
	}


}
