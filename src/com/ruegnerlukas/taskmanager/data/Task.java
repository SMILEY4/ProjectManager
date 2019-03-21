package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;

import java.util.HashMap;
import java.util.Map;

public class Task {

	public Map<TaskAttribute, TaskAttributeValue> attributes = new HashMap<>();




	public int getID() {
		TaskAttribute attribute = Logic.attribute.getAttribute(TaskAttributeType.ID).getValue();
		if(attribute == null) {
			return -1;
		} else {
			NumberValue value = (NumberValue) attributes.get(attribute);
			return value.getInt();
		}
	}


}
