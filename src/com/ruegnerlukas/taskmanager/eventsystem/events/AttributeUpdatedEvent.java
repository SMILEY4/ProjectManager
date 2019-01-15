package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;

public class AttributeUpdatedEvent extends Event {


	private TaskAttribute attribute;
	private TaskAttributeData.Var var;
	private Object newValue;




	public AttributeUpdatedEvent(TaskAttribute attribute, TaskAttributeData.Var var, Object newValue, Object source) {
		super(source);
		this.attribute = attribute;
		this.var = var;
		this.newValue = newValue;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeData.Var getVar() {
		return var;
	}




	public Object getNewValue() {
		return newValue;
	}


}
