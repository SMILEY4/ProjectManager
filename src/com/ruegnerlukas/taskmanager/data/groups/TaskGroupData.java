package com.ruegnerlukas.taskmanager.data.groups;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class TaskGroupData {

	public List<TaskAttribute> attributes = new ArrayList<>();
	public List<TaskGroup> groups = new ArrayList<>();
	public List<Task> tasks = new ArrayList<>();
}
