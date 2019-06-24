package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedList;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectData {


	public final SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes", null);
	public final SyncedList<Task> tasks = new SyncedList<>("project.data.tasks", null);

	public final SyncedProperty<FilterCriteria> filterData = new SyncedProperty<>("project.data.filter", null);
	public final SyncedProperty<TaskGroupData> groupData = new SyncedProperty<>("project.data.group", null);
	public final SyncedProperty<SortData> sortData = new SyncedProperty<>("project.data.sort", null);

	public final SyncedProperty<String> presetSelectedFilter = new SyncedProperty<>("project.data.preset_selected_filter", null);
	public final SyncedProperty<String> presetSelectedGroup = new SyncedProperty<>("project.data.preset_selected_group", null);
	public final SyncedProperty<String> presetSelectedSort = new SyncedProperty<>("project.data.preset_selected_sort", null);
	public final SyncedProperty<String> presetSelectedMaster = new SyncedProperty<>("project.data.preset_selected_master", null);

	public final SyncedMap<String, FilterCriteria> presetsFilter = new SyncedMap<>("project.data.presets_filter", null);
	public final SyncedMap<String, TaskGroupData> presetsGroup = new SyncedMap<>("project.data.presets_group", null);
	public final SyncedMap<String, SortData> presetsSort = new SyncedMap<>("project.data.presets_sort", null);
	public final SyncedMap<String, MasterPreset> presetsMaster = new SyncedMap<>("project.data.presets_master", null);




	public void dispose() {
		attributes.dispose();
		tasks.dispose();
		filterData.dispose();
		groupData.dispose();
		sortData.dispose();
		presetSelectedFilter.dispose();
		presetSelectedGroup.dispose();
		presetSelectedSort.dispose();
		presetSelectedMaster.dispose();
		presetsFilter.dispose();
		presetsGroup.dispose();
		presetsSort.dispose();
		presetsMaster.dispose();
	}

}
