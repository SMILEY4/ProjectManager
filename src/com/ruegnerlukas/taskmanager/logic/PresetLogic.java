package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;

public class PresetLogic {


	public static FilterCriteria getFilterPreset(Project project, String name) {
		return project.data.filterPresets.get(name.trim());
	}




	public static boolean deleteFilterPreset(Project project, String name) {
		return project.data.filterPresets.remove(name.trim()) != null;
	}




	public static boolean saveFilterPreset(Project project, String name, FilterCriteria criteria) {
		if (!project.data.filterPresets.containsKey(name.trim())) {
			project.data.filterPresets.put(name.trim(), criteria);
			return true;
		} else {
			return false;
		}
	}




	public static TaskGroupData getTaskGroupPreset(Project project, String name) {
		return project.data.groupPresets.get(name.trim());
	}




	public static boolean deleteTaskGroupPreset(Project project, String name) {
		return project.data.groupPresets.remove(name.trim()) != null;
	}




	public static boolean saveTaskGroupPreset(Project project, String name, TaskGroupData groupData) {
		if (!project.data.groupPresets.containsKey(name.trim())) {
			project.data.groupPresets.put(name.trim(), groupData);
			return true;
		} else {
			return false;
		}
	}




	public static SortData getSortPreset(Project project, String name) {
		return project.data.sortPresets.get(name.trim());
	}




	public static boolean deleteSortPreset(Project project, String name) {
		return project.data.sortPresets.remove(name.trim()) != null;
	}




	public static boolean saveSortPreset(Project project, String name, SortData sortData) {
		if (!project.data.sortPresets.containsKey(name.trim())) {
			project.data.sortPresets.put(name.trim(), sortData);
			return true;
		} else {
			return false;
		}
	}




	public static MasterPreset getMasterPreset(Project project, String name) {
		return project.data.masterPresets.get(name.trim());
	}




	public static boolean deleteMasterPreset(Project project, String name) {
		return project.data.masterPresets.remove(name.trim()) != null;
	}




	public static boolean saveMasterPreset(Project project, String name, MasterPreset preset) {
		if (!project.data.masterPresets.containsKey(name.trim())) {
			project.data.masterPresets.put(name.trim(), preset);
			return true;
		} else {
			return false;
		}
	}

}
