package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;

public class PresetLogic {


	public static void init() {
	}




	public static FilterCriteria getFilterPreset(Project project, String name) {
		return project.data.presetsFilter.get(name.trim());
	}




	public static boolean deleteFilterPreset(Project project, String name) {
		return project.data.presetsFilter.remove(name.trim()) != null;
	}




	public static boolean saveFilterPreset(Project project, String name, FilterCriteria criteria) {
		if (!project.data.presetsFilter.containsKey(name.trim())) {
			project.data.presetsFilter.put(name.trim(), criteria);
			return true;
		} else {
			return false;
		}
	}




	public static TaskGroupData getTaskGroupPreset(Project project, String name) {
		return project.data.presetsGroup.get(name.trim());
	}




	public static boolean deleteTaskGroupPreset(Project project, String name) {
		return project.data.presetsGroup.remove(name.trim()) != null;
	}




	public static boolean saveTaskGroupPreset(Project project, String name, TaskGroupData groupData) {
		if (!project.data.presetsGroup.containsKey(name.trim())) {
			project.data.presetsGroup.put(name.trim(), groupData);
			return true;
		} else {
			return false;
		}
	}




	public static SortData getSortPreset(Project project, String name) {
		return project.data.presetsSort.get(name.trim());
	}




	public static boolean deleteSortPreset(Project project, String name) {
		return project.data.presetsSort.remove(name.trim()) != null;
	}




	public static boolean saveSortPreset(Project project, String name, SortData sortData) {
		if (!project.data.presetsSort.containsKey(name.trim())) {
			project.data.presetsSort.put(name.trim(), sortData);
			return true;
		} else {
			return false;
		}
	}




	public static MasterPreset getMasterPreset(Project project, String name) {
		return project.data.presetsMaster.get(name.trim());
	}




	public static boolean deleteMasterPreset(Project project, String name) {
		return project.data.presetsMaster.remove(name.trim()) != null;
	}




	public static boolean saveMasterPreset(Project project, String name, MasterPreset preset) {
		if (!project.data.presetsMaster.containsKey(name.trim())) {
			project.data.presetsMaster.put(name.trim(), preset);
			return true;
		} else {
			return false;
		}
	}

}
