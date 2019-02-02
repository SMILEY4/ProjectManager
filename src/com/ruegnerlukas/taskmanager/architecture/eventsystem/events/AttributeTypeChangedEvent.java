package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;

public class AttributeTypeChangedEvent extends Event {


	private TaskAttribute attribute;
	private TaskAttributeType prevType;




	public AttributeTypeChangedEvent(TaskAttribute attribute, TaskAttributeType prevType, Object source) {
		super(source);
		this.attribute = attribute;
		this.prevType = prevType;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeType getPrevType() {
		return prevType;
	}


}
