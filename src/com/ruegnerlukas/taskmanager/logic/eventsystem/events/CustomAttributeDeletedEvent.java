package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeDeletedEvent extends Event {

	private CustomAttribute attrib;
	
	public CustomAttributeDeletedEvent(CustomAttribute attrib, Object source) {
		super(source);
		this.attrib = attrib;
	}
	
	
	public CustomAttribute getDeletedAttribute() {
		return attrib;
	}

	
	
}
