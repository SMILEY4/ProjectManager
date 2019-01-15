package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;

public class AttributeUpdatedRejection extends Event {


	private EventCause cause;
	private TaskAttribute attribute;
	private TaskAttributeData.Var var;
	private Object newValue;




	public AttributeUpdatedRejection(TaskAttribute attribute, TaskAttributeData.Var var,
									 Object newValue, EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.var = var;
		this.newValue = newValue;
		this.cause = cause;
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



	public EventCause getCause() {
		return cause;
	}

}
