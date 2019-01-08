package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeChangedNameEvent extends Event {

	private CustomAttribute attrib;
	private String oldName, newName;
	
	public CustomAttributeChangedNameEvent(CustomAttribute attrib, String oldName, String newName, Object source) {
		super(source);
		this.attrib = attrib;
		this.oldName = oldName;
		this.newName = newName;
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
	
	
}
