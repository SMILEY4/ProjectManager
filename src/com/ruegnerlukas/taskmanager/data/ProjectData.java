package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ProjectData {


	public ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public ObservableList<ObservableMap<TaskAttribute, Object>> tasks = FXCollections.observableArrayList();

}
