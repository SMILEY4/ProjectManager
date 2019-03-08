package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.HashMap;
import java.util.Map;

public class Task {

	public Map<TaskAttribute, TaskAttributeValue> attributes = new HashMap<>();




	public int getID() {
		SyncRequest<TaskAttribute> request = new SyncRequest<>();
		Logic.attribute.getAttribute(TaskAttributeType.ID, request);
		TaskAttribute attribute = request.getResponse().getValue();
		NumberValue value = (NumberValue) attributes.get(attribute);
		return value.getInt();
	}


}
