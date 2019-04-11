package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskAttributeLogic;

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
			project.data.attributes.add(TaskAttributeLogic.createTaskAttribute(type, type.display + " Attribute"));
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




	public static boolean addTaskAttributeToProject(Project project, TaskAttribute attribute) {
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




	public static boolean removeTaskAttributeFromProject(Project project, TaskAttribute attribute) {
		return project.data.attributes.remove(attribute);
	}


}
