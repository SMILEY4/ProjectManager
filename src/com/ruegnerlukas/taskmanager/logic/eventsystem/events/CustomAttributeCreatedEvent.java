package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeCreatedEvent extends Event {

	private CustomAttribute attrib;
	
	public CustomAttributeCreatedEvent(CustomAttribute attrib, Object source) {
		super(source);
		this.attrib = attrib;
	}
	
	
	public CustomAttribute getCreatedAttribute() {
		return attrib;
	}

	
	
}
