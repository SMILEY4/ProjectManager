package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.externaldata.files.ExternalFileHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

public class ProjectLogic {


	public static void init() {
	}




	public static void closeCurrentProject() {
		if(Data.projectProperty.get() != null) {
			Data.projectProperty.get().dispose();
			setCurrentProject(null);
		}
	}




	public static void setCurrentProject(Project project) {
		closeCurrentProject();
		Data.projectProperty.set(project);
	}




	public static Project createNewLocalProject() {
		return createNewLocalProject("New Project");
	}




	public static Project createNewLocalProject(String name) {
		Project project = new Project(new ExternalFileHandler()); // TODO set root directory
		project.settings.name.set(name);
		project.settings.attributesLocked.set(false);
		project.settings.idCounter.set(0);
		for (AttributeType type : AttributeType.getFixedTypes()) {
			project.data.attributes.add(AttributeLogic.createTaskAttribute(type, type.display + " Attribute", project));
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
		lockTaskAttributes(project, !project.settings.attributesLocked.get());
	}




	public static void lockTaskAttributes(Project project, boolean locked) {
		if (project.settings.attributesLocked.get() != locked) {
			project.settings.attributesLocked.set(locked);
		}
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

		if (!project.data.attributes.remove(attribute)) {
			return false;
		}

		// check filter data
		if (project.data.filterData.get() != null) {
			FilterCriteria filterData = project.data.filterData.get();
			if (TaskLogic.getUsedFilterAttributes(filterData).contains(attribute)) {
				project.temporaryData.lastGroupsValid.set(false);
				return true;
			}
		}

		// check group data
		if (project.data.groupData.get() != null) {
			TaskGroupData groupData = project.data.groupData.get();
			if (groupData.attributes.contains(attribute)) {
				project.temporaryData.lastGroupsValid.set(false);
				return true;
			}
		}

		// check sort data
		if (project.data.sortData.get() != null) {
			SortData sortData = project.data.sortData.get();
			for (SortElement element : sortData.sortElements) {
				if (element.attribute.get() == attribute) {
					project.temporaryData.lastGroupsValid.set(false);
					return true;
				}
			}
		}

		return true;
	}




	public static boolean addTaskToProject(Project project, Task task) {
		TaskAttribute attribute = AttributeLogic.findAttribute(project, AttributeType.ID);
		if (TaskLogic.getTaskValue(task, attribute).getAttType() == null) {
			final int id = project.settings.idCounter.get();
			project.settings.idCounter.set(id + 1);
			TaskLogic.setValue(project, task, attribute, new IDValue(id));
		}
		project.data.tasks.add(task);
		return true;
	}




	public static boolean removeTaskFromProject(Project project, Task task) {
		return project.data.tasks.remove(task);
	}


}
