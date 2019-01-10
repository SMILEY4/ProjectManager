package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements.TaskAttributeRequirement;

public class AttributeUpdatedEvent extends Event {


	private TaskAttribute attribute;
	private TaskAttributeRequirement oldRequirement;


	public AttributeUpdatedEvent(TaskAttribute attribute, TaskAttributeRequirement oldRequirement, Object source) {
		super(source);
		this.attribute = attribute;
		this.oldRequirement = oldRequirement;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeRequirement getOldRequirement() {
		return oldRequirement;
	}

}
