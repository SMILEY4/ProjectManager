package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeChangedTypeEvent extends Event {

	private CustomAttribute attrib;
	private Type oldType, newType;
	
	public CustomAttributeChangedTypeEvent(CustomAttribute attrib, Type oldType, Type newType, Object source) {
		super(source);
		this.attrib = attrib;
		this.oldType = oldType;
		this.newType = newType;
	}
	
	
	public CustomAttribute getAttribute() {
		return attrib;
	}

	
	public Type getOldType() {
		return oldType;
	}

	
	public Type getNewType() {
		return newType;
	}
	
	
}
