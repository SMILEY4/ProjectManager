package com.ruegnerlukas.taskmanager.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Map;

public class AttributeUpdatedEvent extends Event {


	private TaskAttribute attribute;
	private Map<TaskAttributeData.Var, TaskAttributeValue> changedVars;
	private Object newMainValue;




	public AttributeUpdatedEvent(TaskAttribute attribute, Map<TaskAttributeData.Var, TaskAttributeValue> changedVars, Object newMainValue, Object source) {
		super(source);
		this.attribute = attribute;
		this.changedVars = changedVars;
		this.newMainValue = newMainValue;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public Map<TaskAttributeData.Var, TaskAttributeValue> getChangedVars() {
		return changedVars;
	}




	public Object getChangedMainValue() {
		return newMainValue;
	}


}
