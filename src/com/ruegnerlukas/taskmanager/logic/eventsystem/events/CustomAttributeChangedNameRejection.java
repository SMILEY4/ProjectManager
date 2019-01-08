package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeChangedNameRejection extends Event {

	private CustomAttribute attrib;
	private String oldName, newName;
	private EventCause cause;
	
	
	public CustomAttributeChangedNameRejection(CustomAttribute attrib, String oldName, String newName, EventCause cause, Object source) {
		super(source);
		this.attrib = attrib;
		this.oldName = oldName;
		this.newName = newName;
		this.cause = cause;
	}
	
	
	public CustomAttribute getAttribute() {
		return attrib;
	}

	
	public String getOldName() {
		return oldName;
	}

	
	public String getNewName() {
		return newName;
	}
	
	
	public EventCause getCause() {
		return cause;
	}
	
}
