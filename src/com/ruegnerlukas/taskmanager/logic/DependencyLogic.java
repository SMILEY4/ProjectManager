package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
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
	protected List<Task> getDependentOnInternal(Task task, TaskAttribute dependencyAttribute) {

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
	 * @return a list of tasks that depend on given task
	 */
	public Response<List<Task>> getDependentOn(Task task, TaskAttribute dependencyAttribute) {
		Project project = Logic.project.getProject();
		if (project != null || dependencyAttribute.data.getType() != TaskAttributeType.DEPENDENCY) {
			return new Response<List<Task>>().complete(getDependentOnInternal(task, dependencyAttribute));
		} else {
			return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	/**
	 * @return a list of tasks that the given task depends on
	 */
	protected List<Task> getPrerequisitesOfInternal(Task task, TaskAttribute dependencyAttribute) {
		TaskArrayValue value = (TaskArrayValue) Logic.tasks.getValue(task, dependencyAttribute);
		return new ArrayList<>(Arrays.asList(value.getTasks()));
	}




	/**
	 * @return a list of tasks that the given task depends on
	 */
	public Response<List<Task>> getPrerequisitesOf(Task task, TaskAttribute dependencyAttribute) {
		Project project = Logic.project.getProject();
		if (project != null || dependencyAttribute.data.getType() != TaskAttributeType.DEPENDENCY) {
			return new Response<List<Task>>().complete(getPrerequisitesOfInternal(task, dependencyAttribute));
		} else {
			return new Response<List<Task>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	public void createDependency(Task task, Task prerequisite, TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (attribute.data.getType() == TaskAttributeType.DEPENDENCY) {
				TaskAttributeValue value = Logic.tasks.getValue(task, attribute);
				TaskArrayValue newValue = (value == null || value instanceof NoValue) ?
						new TaskArrayValue(prerequisite) :
						new TaskArrayValue(((TaskArrayValue) value).getTasks(), prerequisite);
				Logic.tasks.setValue(task, attribute, newValue);
			}
		}
	}




	public void deleteDependency(Task task, Task prerequisite, TaskAttribute attribute) {
		Project project = Logic.project.getProject();
		if (project != null) {
			if (attribute.data.getType() == TaskAttributeType.DEPENDENCY) {
				TaskAttributeValue value = Logic.tasks.getValue(task, attribute);
				TaskArrayValue taskArray = (value == null || value instanceof NoValue) ? new TaskArrayValue() : (TaskArrayValue) value;

				boolean existsDependency = false;
				for (Task t : taskArray.getTasks()) {
					if (t == prerequisite) {
						existsDependency = true;
						break;
					}
				}

				if (existsDependency) {
					List<Task> newList = new ArrayList<>();
					for (Task t : taskArray.getTasks()) {
						if (t != prerequisite) {
							newList.add(t);
						} else {
						}
					}
					Logic.tasks.setValue(task, attribute, new TaskArrayValue(newList));
				}

			}
		}
	}


}
