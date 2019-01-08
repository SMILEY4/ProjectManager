package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Variable;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class CustomAttributeChangedVariableEvent extends Event {

	private CustomAttribute attrib;
	private Variable variable;
	private Object oldValue, newValue;
	
	public CustomAttributeChangedVariableEvent(CustomAttribute attrib, Variable variable, Object oldValue, Object newValue, Object source) {
		super(source);
		this.attrib = attrib;
		this.variable = variable;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	
	public CustomAttribute getAttribute() {
		return attrib;
	}

	
	public Variable getVariable() {
		return variable;
	}
	
	
	public Object getOldValue() {
		return oldValue;
	}

	
	public Object getNewValue() {
		return newValue;
	}
	
	
	@Override
	public String toString() {
		return "CustomAttributeChangedVariableEvent@" + this.hashCode() + ":  " + variable + " = " + newValue;
	}
	
}
