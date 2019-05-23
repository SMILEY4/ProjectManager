package com.ruegnerlukas.taskmanager.logic.events;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class AttributeValueChangeEvent extends Event {


	public static final EventType<AttributeValueChangeEvent> ATTVALUE_CHANGED =
			new EventType<>(Event.ANY, "ATTVALUE_CHANGED");


	private TaskAttribute attribute;
	private AttributeValueType type;
	private AttributeValue<?> prevValue;
	private AttributeValue<?> newValue;




	public AttributeValueChangeEvent(TaskAttribute attribute, AttributeValueType type, AttributeValue<?> prevValue, AttributeValue<?> newValue) {
		super(ATTVALUE_CHANGED);
		this.attribute = attribute;
		this.type = type;
		this.prevValue = prevValue;
		this.newValue = newValue;
	}




	public AttributeValueChangeEvent(Object source, EventTarget target, TaskAttribute attribute, AttributeValueType type, AttributeValue<?> prevValue, AttributeValue<?> newValue) {
		super(source, target, ATTVALUE_CHANGED);
		this.attribute = attribute;
		this.type = type;
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




	public AttributeValueType getValueType() {
		return type;
	}




	public AttributeValue<?> getPrevValue() {
		return prevValue;
	}




	public AttributeValue<?> getNewValue() {
		return newValue;
	}


}
