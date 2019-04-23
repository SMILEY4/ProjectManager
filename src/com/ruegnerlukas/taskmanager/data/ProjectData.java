package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.projectdata.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ProjectData {


	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

	public final CustomProperty<FilterCriteria> filterCriteria = new CustomProperty<>();
	public final CustomProperty<TaskGroupData> groupData = new CustomProperty<>();
	public final ObservableList<SortElement> sortElements = FXCollections.observableArrayList();

	public final ObservableMap<String, FilterCriteria> filterPresets = FXCollections.observableHashMap();
	public final ObservableMap<String, TaskGroupData> groupPresets = FXCollections.observableHashMap();

}
