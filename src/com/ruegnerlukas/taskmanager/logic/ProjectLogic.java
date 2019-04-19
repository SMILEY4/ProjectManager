package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

public class ProjectLogic {


	public static void closeCurrentProject() {
		setCurrentProject(null);
	}




	public static void setCurrentProject(Project project) {
		Data.projectProperty.set(project);
	}




	public static Project createNewProject() {
		return createNewProject("New Project");
	}




	public static Project createNewProject(String name) {
		Project project = new Project();
		project.settings.name.set(name);
		for (AttributeType type : AttributeType.getFixedTypes()) {
			project.data.attributes.add(AttributeLogic.createTaskAttribute(type, type.display + " Attribute"));
		}
		return project;
	}




	public static void saveProject(Project project) {
		System.out.println("TODO: save current project."); // TODO
	}




	public static void renameProject(Project project, String newName) {
		project.settings.name.set(newName);
	}




	public static void lockSwitchTaskAttributes(Project project) {
		project.settings.attributesLocked.set(!project.settings.attributesLocked.get());
	}




	public static boolean addAttributeToProject(Project project, TaskAttribute attribute) {
		// check name
		for (TaskAttribute att : project.data.attributes) {
			if (att.name.get().equals(attribute.name.get())) {
				return false;
			}
		}
		// check type
		if (attribute.type.get().fixed) {
			for (TaskAttribute att : project.data.attributes) {
				if (att.type.get() == attribute.type.get()) {
					return false;
				}
			}
		}
		// add attribute
		project.data.attributes.add(attribute);
		return true;
	}




	public static boolean removeAttributeFromProject(Project project, TaskAttribute attribute) {
		return project.data.attributes.remove(attribute);
	}




	public static boolean addTaskToProject(Project project, Task task) {
		TaskAttribute attribute = AttributeLogic.findAttribute(project, AttributeType.ID);
		if (TaskLogic.getTrueValue(task, attribute, Integer.class) == null) {
			final int id = project.settings.idCounter.get();
			project.settings.idCounter.set(id + 1);
			TaskLogic.setValue(task, attribute, id);
		}
		project.data.tasks.add(task);
		return true;
	}




	public static boolean removeTaskFromProject(Project project, Task task) {
		return project.data.tasks.remove(task);
	}


}
