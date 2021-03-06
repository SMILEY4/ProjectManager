package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.externaldata.files.ExternalFileHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.io.File;

public class ProjectLogic {


	public static void init() {
	}




	/**
	 * Closes the currently active {@link Project}
	 */
	public static void closeCurrentProject() {
		if (Data.projectProperty.get() != null) {
			setCurrentProject(null);
		}
	}




	/**
	 * Closes the active {@link Project} and sets the given {@link Project} to the new active {@link Project}.
	 */
	public static void setCurrentProject(Project project) {
		if (Data.projectProperty.get() != null) {
			Data.projectProperty.get().dispose();
		}
		Data.projectProperty.set(project);
	}




	/**
	 * @return the local project with the given {@link File} as the root directory.
	 */
	public static Project loadLocalProject(File directory) {
		ExternalFileHandler handler = new ExternalFileHandler(directory.getAbsolutePath());
		Project project = handler.loadProject();
		Logger.get().info("Local Project loaded (" + project.settings.name.get() + ")." + System.lineSeparator() + directory.getAbsolutePath());
		return project;
	}




	/**
	 * @return a new {@link Project} in the given directory with a generic name and an {@link ExternalFileHandler}
	 */
	public static Project createNewLocalProject(File directory) {
		return createNewLocalProject(directory, "New Project");
	}




	/**
	 * @return a new {@link Project} in the given directory with the given name and an {@link ExternalFileHandler}.
	 */
	public static Project createNewLocalProject(File directory, String name) {
		Project project = new Project(new ExternalFileHandler(directory.getAbsolutePath()));
		project.settings.name.set(name);
		project.settings.attributesLocked.set(false);
		project.settings.taskIDCounter.set(0);
		project.settings.attIDCounter.set(0);
		for (AttributeType type : AttributeType.getFixedTypes()) {
			project.data.attributes.add(AttributeLogic.createTaskAttribute(type, type.display + " Attribute", project));
		}
		Logger.get().info("Local Project created (" + name + ")." + System.lineSeparator() + directory.getAbsolutePath());
		return project;
	}




	/**
	 * sets the name of the given {@link Project} to the given name.
	 */
	public static void renameProject(Project project, String newName) {
		project.settings.name.set(newName);
	}




	/**
	 * locks/unlocks the {@link TaskAttribute}s of the given {@link Project} if they are currently unlocked/locked.
	 */
	public static void lockSwitchTaskAttributes(Project project) {
		lockTaskAttributes(project, !project.settings.attributesLocked.get());
	}




	/**
	 * lock/unlock the {@link TaskAttribute}s of the given {@link Project}
	 */
	public static void lockTaskAttributes(Project project, boolean locked) {
		if (project.settings.attributesLocked.get() != locked) {
			project.settings.attributesLocked.set(locked);
		}
	}




	/**
	 * Adds the given {@link TaskAttribute} to the given {@link Project}.
	 *
	 * @return true, if the attribute is valid and was added
	 */
	public static boolean addAttributeToProject(Project project, TaskAttribute attribute) {

		// check id
		for (TaskAttribute att : project.data.attributes) {
			if (att.id == attribute.id) {
				Logger.get().warn("Could not add Attribute \"" + attribute.name.get() + "\". Attribute with same id already exists.");
				return false;
			}
		}
		// check type
		if (attribute.type.get().fixed) {
			for (TaskAttribute att : project.data.attributes) {
				if (att.type.get() == attribute.type.get()) {
					Logger.get().warn("Could not add Attribute \"" + attribute.name.get() + "\". Attribute of same type already exists.");
					return false;
				}
			}
		}
		// add attribute
		project.data.attributes.add(attribute);
		return true;
	}




	/**
	 * Removes the given {@link TaskAttribute} from the given {@link Project}. <br>
	 * Also removes the attribute from all {@link Task}s and updates the active filter-,group- and sort-data.
	 *
	 * @return true, if the attribute was successfully removed
	 */
	public static boolean removeAttributeFromProject(Project project, TaskAttribute attribute) {

		if (!project.data.attributes.remove(attribute)) {
			Logger.get().warn("Could not remove Attribute \"" + attribute.name.get() + "\". Attribute is fixed type.");
			return false;
		}

		// check tasks
		for (Task task : project.data.tasks) {
			task.values.remove(attribute);
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




	/**
	 * Adds the given {@link Task} to the given {@link Project} and gives the task a new valid id (if missing).
	 */
	public static boolean addTaskToProject(Project project, Task task) {
		TaskAttribute attribute = AttributeLogic.findAttributeByType(project, AttributeType.ID);
		if (TaskLogic.getTaskValue(task, attribute).getAttType() == null) {
			final int id = project.settings.taskIDCounter.get();
			project.settings.taskIDCounter.set(id + 1);
			TaskLogic.setValue(project, task, attribute, new IDValue(id));
		}
		project.data.tasks.add(task);
		return true;
	}




	/**
	 * Removes the given {@link Task} from the given {@link Project}.
	 *
	 * @return true, if the task was successfully removed
	 */
	public static boolean removeTaskFromProject(Project project, Task task) {
		return project.data.tasks.remove(task);
	}




	/**
	 * Adds the given {@link DocumentationFile} to the given {@link Project}.
	 */
	public static boolean addDocumentationToProject(Project project, DocumentationFile doc) {
		project.data.docFiles.add(doc);
		return true;
	}




	/**
	 * Removes the given {@link DocumentationFile} from the given {@link Project}.
	 *
	 * @return true, if the doc file was successfully removed
	 */
	public static boolean removeDocumentationFromProject(Project project, DocumentationFile doc) {
		return project.data.docFiles.remove(doc);
	}

}
