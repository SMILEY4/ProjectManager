package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class AttributeRenamedEvent extends Event {


	private TaskAttribute attribute;
	private String oldName;



	public AttributeRenamedEvent(TaskAttribute attribute, String oldName, Object source) {
		super(source);
		this.attribute = attribute;
		this.oldName = oldName;
	}




	public TaskAttribute getAttribute() {
		return this.attribute;
	}




	public String getOldName() {
		return oldName;
	}

}
