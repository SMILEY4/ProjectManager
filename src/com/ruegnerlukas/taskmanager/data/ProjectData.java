package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.projectdata.SortElement;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectData {


	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

	public final CustomProperty<FilterCriteria> filterCriteria = new CustomProperty<>();
	public final ObservableList<TaskAttribute> groupBy = FXCollections.observableArrayList();
	public final ObservableList<SortElement> sortElements = FXCollections.observableArrayList();

}
