package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;

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


}
