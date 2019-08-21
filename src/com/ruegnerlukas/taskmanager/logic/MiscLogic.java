package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;

public class MiscLogic {


	public static void init() {
	}




	/**
	 * @return the filter-preset of the given {@link Project} with the given name or null
	 */
	public static FilterCriteria getFilterPreset(Project project, String name) {
		return project.data.presetsFilter.get(name.trim());
	}




	/**
	 * Removes the filter-preset with the given name from the given {@link Project}
	 */
	public static boolean deleteFilterPreset(Project project, String name) {
		boolean removed = project.data.presetsFilter.remove(name.trim()) != null;
		if (removed) {
			Logger.get().info("Removed Filter-Preset (" + name + ").");
		}
		return removed;
	}




	/**
	 * Saves the given {@link FilterCriteria} as a new preset with the given name in the given {@link Project}
	 *
	 * @return true, if the preset was saved, false if a preset with the given name already exists.
	 */
	public static boolean saveFilterPreset(Project project, String name, FilterCriteria criteria) {
		if (!project.data.presetsFilter.containsKey(name.trim())) {
			project.data.presetsFilter.put(name.trim(), criteria);
			Logger.get().info("Saved Filter-Preset (" + name + ").");
			return true;
		} else {
			Logger.get().info("Could not save Filter-Preset (" + name + "). Preset with name already exists.");
			return false;
		}
	}




	/**
	 * @return the group-preset of the given {@link Project} with the given name or null
	 */
	public static TaskGroupData getTaskGroupPreset(Project project, String name) {
		return project.data.presetsGroup.get(name.trim());
	}




	/**
	 * Removes the group-preset with the given name from the given {@link Project}
	 */
	public static boolean deleteTaskGroupPreset(Project project, String name) {
		boolean removed = project.data.presetsGroup.remove(name.trim()) != null;
		if (removed) {
			Logger.get().info("Removed Group-Preset (" + name + ").");
		}
		return removed;
	}




	/**
	 * Saves the given {@link TaskGroupData} as a new preset with the given name in the given {@link Project}
	 *
	 * @return true, if the preset was saved, false if a preset with the given name already exists.
	 */
	public static boolean saveTaskGroupPreset(Project project, String name, TaskGroupData groupData) {
		if (!project.data.presetsGroup.containsKey(name.trim())) {
			project.data.presetsGroup.put(name.trim(), groupData);
			Logger.get().info("Saved Group-Preset (" + name + ").");
			return true;
		} else {
			Logger.get().info("Could not save Group-Preset (" + name + "). Preset with name already exists.");
			return false;
		}
	}




	/**
	 * @return the sort-preset of the given {@link Project} with the given name or null
	 */
	public static SortData getSortPreset(Project project, String name) {
		return project.data.presetsSort.get(name.trim());
	}




	/**
	 * Removes the sort-preset with the given name from the given {@link Project}
	 */
	public static boolean deleteSortPreset(Project project, String name) {
		boolean removed = project.data.presetsSort.remove(name.trim()) != null;
		if (removed) {
			Logger.get().info("Removed Sort-Preset (" + name + ").");
		}
		return removed;
	}




	/**
	 * Saves the given {@link SortData} as a new preset with the given name in the given {@link Project}
	 *
	 * @return true, if the preset was saved, false if a preset with the given name already exists.
	 */
	public static boolean saveSortPreset(Project project, String name, SortData sortData) {
		if (!project.data.presetsSort.containsKey(name.trim())) {
			project.data.presetsSort.put(name.trim(), sortData);
			Logger.get().info("Saved Sort-Preset (" + name + ").");
			return true;
		} else {
			Logger.get().info("Could not save Sort-Preset (" + name + "). Preset with name already exists.");
			return false;
		}
	}




	/**
	 * @return the master-preset of the given {@link Project} with the given name or null
	 */
	public static MasterPreset getMasterPreset(Project project, String name) {
		return project.data.presetsMaster.get(name.trim());
	}




	/**
	 * Removes the master-preset with the given name from the given {@link Project}
	 */
	public static boolean deleteMasterPreset(Project project, String name) {
		boolean removed = project.data.presetsMaster.remove(name.trim()) != null;
		if (removed) {
			Logger.get().info("Removed Master-Preset (" + name + ").");
		}
		return removed;
	}




	/**
	 * Saves the given {@link MasterPreset} as a new preset with the given name in the given {@link Project}
	 *
	 * @return true, if the preset was saved, false if a preset with the given name already exists.
	 */
	public static boolean saveMasterPreset(Project project, String name, MasterPreset preset) {
		if (!project.data.presetsMaster.containsKey(name.trim())) {
			project.data.presetsMaster.put(name.trim(), preset);
			Logger.get().info("Saved Master-Preset (" + name + ").");
			return true;
		} else {
			Logger.get().info("Could not save Master-Preset (" + name + "). Preset with name already exists.");
			return false;
		}
	}




	/**
	 * Creates a new valid {@link DocumentationFile} for the given {@link Project}. The new {@link DocumentationFile} will not be added to the given {@link Project}.
	 *
	 * @return the created {@link DocumentationFile}
	 */
	public static DocumentationFile createDocFile(Project project, String name) {
		final int id = project.settings.docIDCounter.get();
		System.out.println("CREATE DOC " + id);
		project.settings.docIDCounter.set(id + 1);
		return new DocumentationFile(id, name, project);
	}




	public static DocumentationFile findDocByID(Project project, int id) {
		for (DocumentationFile docFile : project.data.docFiles) {
			if (docFile.id.get() == id) {
				return docFile;
			}
		}
		return null;
	}

}
