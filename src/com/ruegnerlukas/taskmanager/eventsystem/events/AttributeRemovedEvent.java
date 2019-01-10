package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;

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
