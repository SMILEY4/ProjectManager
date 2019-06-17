package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedList;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ProjectData {


	public final SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes");

	//	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

	public final CustomProperty<FilterCriteria> filterData = new CustomProperty<>();
	public final CustomProperty<TaskGroupData> groupData = new CustomProperty<>();
	public final CustomProperty<SortData> sortData = new CustomProperty<>();

	public final SyncedProperty<String> selectedFilterPreset = new SyncedProperty<>("project.data.preset_selected_filter");
	public final SyncedProperty<String> selectedGroupPreset = new SyncedProperty<>("project.data.preset_selected_group");
	public final SyncedProperty<String> selectedSortPreset = new SyncedProperty<>("project.data.preset_selected_sort");
	public final SyncedProperty<String> selectedMasterPreset = new SyncedProperty<>("project.data.preset_selected_master");

	public final ObservableMap<String, FilterCriteria> filterPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, TaskGroupData> groupPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, SortData> sortPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, MasterPreset> masterPresets = FXCollections.observableHashMap();




	public void dispose() {
		selectedFilterPreset.dispose();
		selectedGroupPreset.dispose();
		selectedSortPreset.dispose();
		selectedMasterPreset.dispose();
	}

}
