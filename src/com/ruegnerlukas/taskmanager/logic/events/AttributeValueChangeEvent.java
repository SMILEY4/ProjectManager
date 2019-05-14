package com.ruegnerlukas.taskmanager.logic.events;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class AttributeValueChangeEvent extends Event {


	public static final EventType<AttributeValueChangeEvent> ATTVALUE_CHANGED =
			new EventType<>(Event.ANY, "ATTVALUE_CHANGED");


	private TaskAttribute attribute;
	private String key;
	private Object prevValue;
	private Object newValue;




	public AttributeValueChangeEvent(TaskAttribute attribute, String key, Object prevValue, Object newValue) {
		super(ATTVALUE_CHANGED);
		this.attribute = attribute;
		this.key = key;
		this.prevValue = prevValue;
		this.newValue = newValue;
	}




	public AttributeValueChangeEvent(Object source, EventTarget target, TaskAttribute attribute, String key, Object prevValue, Object newValue) {
		super(source, target, ATTVALUE_CHANGED);
		this.attribute = attribute;
		this.key = key;
		this.prevValue = prevValue;
		this.newValue = newValue;
	}




	@Override
	public AttributeValueChangeEvent copyFor(Object newSource, EventTarget newTarget) {
		return (AttributeValueChangeEvent) super.copyFor(newSource, newTarget);
	}




	@Override
	public EventType<? extends AttributeValueChangeEvent> getEventType() {
		return (EventType<? extends AttributeValueChangeEvent>) super.getEventType();
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public String getKey() {
		return key;
	}




	public Object getPrevValue() {
		return prevValue;
	}




	public Object getNewValue() {
		return newValue;
	}


}
