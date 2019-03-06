package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Map;

public class AttributeUpdatedRejection extends Event {


	private EventCause cause;
	private TaskAttribute attribute;
	private Map<TaskAttributeData.Var, TaskAttributeValue> values;




	public AttributeUpdatedRejection(
			TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> values, EventCause cause, Object source) {
		super(source);
		this.attribute = attribute;
		this.values = values;
		this.cause = cause;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public Map<TaskAttributeData.Var, TaskAttributeValue> getValues() {
		return values;
	}




	public EventCause getCause() {
		return cause;
	}

}
