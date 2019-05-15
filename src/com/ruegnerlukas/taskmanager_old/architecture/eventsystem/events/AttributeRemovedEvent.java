package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class AttributeRemovedEvent extends Event {


	private TaskAttribute attribute;




	public AttributeRemovedEvent(TaskAttribute attribute, Object source) {
		super(source);
		this.attribute = attribute;
	}




	public TaskAttribute getAttribute() {
		return this.attribute;
	}


}
