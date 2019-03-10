package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DependencyAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependencyLogic {


	/**
	 * @return a list of tasks that depend on given task
	 */
	public List<Task> getDependentOnInternal(Task task, TaskAttribute dependencyAttribute) {

		List<Task> list = new ArrayList<>();

		List<Task> tasks = Logic.tasks.getTasksInternal();
		for (Task t : tasks) {
			TaskArrayValue value = (TaskArrayValue) Logic.tasks.getValue(t, dependencyAttribute);
			for (Task d : value.getTasks()) {
				if (d.equals(task)) {
					list.add(t);
					break;
				}
			}
		}

		return list;
	}




	/**
	 * @return a list of tasks that the given task depends on
	 */
	public List<Task> getPrerequisitesOfInternal(Task task, TaskAttribute dependencyAttribute) {
		TaskArrayValue value = (TaskArrayValue) Logic.tasks.getValue(task, dependencyAttribute);
		return new ArrayList<>(Arrays.asList(value.getTasks()));
	}




	public void createDependency(Task task, Task prerequisite, TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (project.tasks.contains(task) && project.tasks.contains(prerequisite) && attribute.data.getType() == TaskAttributeType.DEPENDENCY) {
				TaskAttributeValue value = Logic.tasks.getValue(task, attribute);
				TaskArrayValue newValue = (value == null || value instanceof NoValue) ?
						new TaskArrayValue(prerequisite) :
						new TaskArrayValue(((TaskArrayValue)value).getTasks(), prerequisite);
				Logic.tasks.setValue(task, attribute, newValue);
			}
		}
	}




	public void deleteDependency(Task task, Task prerequisite, DependencyAttributeData dependency) {
		// TODO
	}


}
