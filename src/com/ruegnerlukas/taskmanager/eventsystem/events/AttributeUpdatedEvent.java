package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;

public class AttributeUpdatedEvent extends Event {


	private TaskAttribute attribute;
	private TaskAttributeData.Var[] changedVars;
	private Object newMainValue;




	public AttributeUpdatedEvent(TaskAttribute attribute, TaskAttributeData.Var[] changedVars, Object newMainValue, Object source) {
		super(source);
		this.attribute = attribute;
		this.changedVars = changedVars;
		this.newMainValue = newMainValue;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeData.Var[] getChangedVars() {
		return changedVars;
	}



	public boolean wasChanged(TaskAttributeData.Var var) {
		for(int i=0; i<getChangedVars().length; i++) {
			if(getChangedVars()[i] == var) {
				return true;
			}
		}
		return false;
	}



	public Object getNewMainValue() {
		return newMainValue;
	}


}
