package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedList;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class ProjectData {


	public final SyncedList<TaskAttribute> attributes;
	public final SyncedList<Task> tasks;
	public final SyncedList<DocumentationFile> docFiles;

	public final CustomProperty<FilterCriteria> filterData;
	public final CustomProperty<TaskGroupData> groupData;
	public final CustomProperty<SortData> sortData;

	public final CustomProperty<String> presetSelectedFilter;
	public final CustomProperty<String> presetSelectedGroup;
	public final CustomProperty<String> presetSelectedSort;
	public final CustomProperty<String> presetSelectedMaster;

	public final SyncedMap<String, FilterCriteria> presetsFilter;
	public final SyncedMap<String, TaskGroupData> presetsGroup;
	public final SyncedMap<String, SortData> presetsSort;
	public final SyncedMap<String, MasterPreset> presetsMaster;




	ProjectData(Project project) {

		attributes = new SyncedList<>(Identifiers.DATA_ATTRIBUTE_LIST, null, project.dataHandler);
		tasks = new SyncedList<>(Identifiers.DATA_TASK_LIST, null, project.dataHandler);
		docFiles = new SyncedList<>(Identifiers.DATA_DOC_FILE_LIST, null, project.dataHandler);

		filterData = new CustomProperty<>();
		groupData = new CustomProperty<>();
		sortData = new CustomProperty<>();

		presetSelectedFilter = new CustomProperty<>();
		presetSelectedGroup = new CustomProperty<>();
		presetSelectedSort = new CustomProperty<>();
		presetSelectedMaster = new CustomProperty<>();

		presetsFilter = new SyncedMap<>(Identifiers.DATA_PRESETS_FILTER, null, project.dataHandler);
		presetsGroup = new SyncedMap<>(Identifiers.DATA_PRESETS_GROUP, null, project.dataHandler);
		presetsSort = new SyncedMap<>(Identifiers.DATA_PRESETS_SORT, null, project.dataHandler);
		presetsMaster = new SyncedMap<>(Identifiers.DATA_PRESETS_MASTER, null, project.dataHandler);

	}




	public void dispose() {
		attributes.dispose();
		tasks.dispose();
		docFiles.dispose();
		presetsFilter.dispose();
		presetsGroup.dispose();
		presetsSort.dispose();
		presetsMaster.dispose();
	}

}
