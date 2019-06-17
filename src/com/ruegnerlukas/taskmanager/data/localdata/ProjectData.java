package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ProjectData {



//	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
//	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

	public final CustomProperty<FilterCriteria> filterData = new CustomProperty<>();
	public final CustomProperty<TaskGroupData> groupData = new CustomProperty<>();
	public final CustomProperty<SortData> sortData = new CustomProperty<>();

	public final SimpleStringProperty selectedFilterPreset = new SimpleStringProperty();
	public final SimpleStringProperty selectedGroupPreset = new SimpleStringProperty();
	public final SimpleStringProperty selectedSortPreset = new SimpleStringProperty();
	public final SimpleStringProperty selectedMasterPreset = new SimpleStringProperty();

	public final ObservableMap<String, FilterCriteria> filterPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, TaskGroupData> groupPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, SortData> sortPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, MasterPreset> masterPresets = FXCollections.observableHashMap();




	public void dispose() {
		// TODO
	}

}
