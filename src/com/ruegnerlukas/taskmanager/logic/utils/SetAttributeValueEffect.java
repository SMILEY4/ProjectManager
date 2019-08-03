package com.ruegnerlukas.taskmanager.logic.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

public class SetAttributeValueEffect {


	public final TaskAttribute attribute;
	public final AttributeValue<?> prevAttValue;
	public final AttributeValue<?> nextAttValue;

	public final Task task;
	public final TaskValue<?> prevTaskValue;
	public final TaskValue<?> nextTaskValue;




	public SetAttributeValueEffect(TaskAttribute attribute, AttributeValue<?> prevAttValue, AttributeValue<?> nextAttValue,
								   Task task, TaskValue<?> prevTaskValue, TaskValue<?> nextTaskValue) {
		this.attribute = attribute;
		this.prevAttValue = prevAttValue;
		this.nextAttValue = nextAttValue;
		this.task = task;
		this.prevTaskValue = prevTaskValue;
		this.nextTaskValue = nextTaskValue;
	}


}
