package com.ruegnerlukas.taskmanager.data.groups;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGroup {

	public Map<TaskAttribute, TaskAttributeValue> values = new HashMap<>();
	public List<Task> tasks = new ArrayList<>();

}
