package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements.TaskAttributeRequirement;

public class AttributeUpdatedRejection extends Event {


	private TaskAttribute attribute;
	private TaskAttributeRequirement newRequirement;
	private EventCause cause;


	public AttributeUpdatedRejection(TaskAttribute attribute, TaskAttributeRequirement newRequirement, EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.newRequirement = newRequirement;
		this.cause = cause;
	}





	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeRequirement getNewRequirement() {
		return newRequirement;
	}




	public EventCause getCause() {
		return cause;
	}

}
