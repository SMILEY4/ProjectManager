package com.ruegnerlukas.taskmanager.logic.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

public class SetAttributeValueEffect {


	public final TaskAttribute attribute;
	public final Task task;
	public final boolean isPrevDefault;
	public final boolean isNextDefault;
	public final TaskValue<?> prevTaskValue;
	public final TaskValue<?> nextTaskValue;




	public SetAttributeValueEffect(TaskAttribute attribute, Task task, boolean isPrevDefault, boolean isNextDefault, TaskValue<?> prevTaskValue, TaskValue<?> nextTaskValue) {
		this.attribute = attribute;
		this.isPrevDefault = isPrevDefault;
		this.isNextDefault = isNextDefault;
		this.task = task;
		this.prevTaskValue = prevTaskValue;
		this.nextTaskValue = nextTaskValue;
	}


}
