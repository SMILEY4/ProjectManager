package com.ruegnerlukas.taskmanager.logic.data.groups;

import com.ruegnerlukas.taskmanager.logic.data.Task;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGroup {

	public Map<TaskAttribute, TaskAttributeValue> attributes = new HashMap<>();
	public List<Task> tasks = new ArrayList<>();

}