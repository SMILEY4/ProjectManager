package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;

public class AttributeTypeChangedEvent extends Event {


	private TaskAttribute attribute;
	private TaskAttributeType oldType;



	public AttributeTypeChangedEvent(TaskAttribute attribute, TaskAttributeType oldType, Object source) {
		super(source);
		this.attribute = attribute;
		this.oldType = oldType;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeType getOldType() {
		return oldType;
	}


}
