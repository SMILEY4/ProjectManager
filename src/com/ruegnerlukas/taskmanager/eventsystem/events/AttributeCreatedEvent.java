package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;

public class AttributeCreatedEvent extends Event {


	private TaskAttribute attribute;




	public AttributeCreatedEvent(TaskAttribute attribute, Object source) {
		super(source);
		this.attribute = attribute;
	}




	public TaskAttribute getAttribute() {
		return this.attribute;
	}


}
